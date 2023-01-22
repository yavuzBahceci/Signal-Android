package com.app.signal.control_kit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import com.app.signal.control_kit.ex.setTextAppearance
import com.app.signal.control_kit.ex.setTextColor
import com.app.signal.design_system.R.*
import com.app.signal.design_system.TextAppearance
import com.app.signal.design_system.ThemeColor
import com.app.signal.design_system.Weight


typealias OnSegmentSelectedListener = (SwitchItem) -> Unit

class SwitchLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    @get: IdRes
    var selectionId: Int = 0
        set(value) {
            field = value
            selectSegment(value)
        }

    private var _container: LinearLayoutCompat
    private val _focusedTabView: View
    private var _listener: OnSegmentSelectedListener? = null

    init {
        setBackgroundResource(R.drawable.shape_segmented_control_bg)

        _focusedTabView = View(context).also {
            it.setBackgroundResource(R.drawable.shape_segmented_control_segment)
            super.addView(it, 0, 0)
        }

        _container = LinearLayoutCompat(context).also {
            it.orientation = LinearLayoutCompat.HORIZONTAL

            super.addView(
                it,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
        }

        val padding = resources
            .getDimension(dimen.spacing_xs)
            .toInt()

        setPadding(padding)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is SwitchItem) {
            addSegmentFromItemView(child)
        } else {
            super.addView(child, index, params)
        }
    }

    private fun addSegmentFromItemView(child: SwitchItem) {
        child.isSelected = false
        child.setOnClickListener {
            selectionId = it.id
            _listener?.invoke(it as SwitchItem)
        }

        _container.addView(
            child,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        val calculatedWeight = 1f / _container.childCount

        _container.children.forEach {
            it.updateLayoutParams<LinearLayoutCompat.LayoutParams> {
                weight = calculatedWeight
            }
        }


        if (selectionId == 0) {
            selectionId = child.id
        }
    }

    fun setOnSegmentedSelectedListener(listener: OnSegmentSelectedListener) {
        this._listener = listener
    }

    fun selectSegment(@IdRes segmentId: Int) {
        _container.children.forEach {
            val isSelected = it.id == segmentId
            it.isSelected = isSelected

            if (isSelected) {
                it.doOnPreDraw(::animateFocus)
            }
        }

        if (!isInLayout) {
            requestLayout()
        }
    }

    private fun animateFocus(item: View) {
        _focusedTabView.layoutParams = LayoutParams(
            item.measuredWidth,
            item.measuredHeight
        )

        _focusedTabView
            .animate()
            .setDuration(
                resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            )
            .setInterpolator(DecelerateInterpolator())
            .x(item.x + paddingStart)
            .start()
    }
}

class SwitchItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {
    private val animationDuration = resources
        .getInteger(android.R.integer.config_shortAnimTime)
        .toLong()

    init {
        setTextAppearance(TextAppearance.Footnote(Weight.Regular))
        setTextColor(ThemeColor.Content.Normal)

        textAlignment = TEXT_ALIGNMENT_CENTER
        textDirection = TEXT_DIRECTION_LOCALE
        includeFontPadding = false

        val vertical = resources.getDimension(dimen.spacing_xs).toInt()
        val horizontal = resources.getDimension(dimen.spacing_xxxl).toInt()

        setPadding(
            horizontal,
            vertical,
            horizontal,
            vertical
        )
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)

        val opacity: Float
        val scale: Float

        if (pressed) {
            opacity = 0.6f
            scale = 0.90f
        } else {
            opacity = 1f
            scale = 1f
        }

        animate()
            .alpha(opacity)
            .scaleX(scale)
            .scaleY(scale)
            .setInterpolator(DecelerateInterpolator())
            .setDuration(animationDuration)
            .start()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        val textColor = if (selected) {
            ThemeColor.Content.Normal
        } else {
            ThemeColor.Content.Opacity30
        }

        setTextColor(textColor)
    }
}