package com.app.signal.control_kit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.*
import com.app.signal.control_kit.ex.resolveColor
import com.app.signal.control_kit.ex.setTextAppearance
import com.app.signal.control_kit.ex.setTextColor
import com.app.signal.design_system.R.*
import com.app.signal.design_system.TextAppearance
import com.app.signal.design_system.ThemeColor
import com.app.signal.design_system.Weight
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.math.abs

typealias OnTabSelectedListener = (TabMenu, Int) -> Unit

class TabMenu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs) {
    enum class DistributionMode {
        FillEqually,
        SpaceBetween
    }

    var forceCenterGravity = false

    var tabs: List<String> = emptyList()
        set(value) {
            field = value
            reload()
        }

    var selectedTab: Int = -1
        set(value) {
            field = value
            selectTab(value, false)
        }

    var distributionMode: DistributionMode
        get() = _container.distributionMode
        set(value) {
            _container.distributionMode = value
            reload()
        }

    var hasUnderline: Boolean
        get() = _highlighter.hasUnderline
        set(value) {
            _highlighter.hasUnderline = value
        }

    var underlineColor: Int
        get() = _highlighter.underlineColor
        set(value) {
            _highlighter.underlineColor = value
        }

    private val _container: TabsContainerView
    private val _highlighter: Highlighter

    private var _listener: OnTabSelectedListener? = null

    init {
        orientation = VERTICAL

        _container = TabsContainerView(context).also {
            addView(
                it,
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }

        _highlighter = Highlighter(context).also {
            val h = it.resources
                .getDimension(dimen.stroke_regular)
                .toInt()

            addView(
                it,
                ViewGroup.LayoutParams.MATCH_PARENT,
                h
            )
        }

        applyAttributes(attrs)

        reload()
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        val attributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TabMenu,
            0,
            0
        )

        hasUnderline = attributes.getBoolean(
            R.styleable.TabMenu_hasUnderline,
            false
        )

        attributes.recycle()
    }

    private fun reload() {
        val delta = tabs.size - _container.childCount

        for (i in 0 until delta) {
            _container.addView(
                TabItem(context),
                WRAP_CONTENT,
                WRAP_CONTENT
            )
        }

        if (delta < 0) {
            _container.removeViews(0, abs(delta))
        }

        val tabsCount = _container.childCount - 1

        if (tabsCount < 1) {
            selectedTab = -1
            return
        }

        tabs.forEachIndexed { idx, text ->
            val tabItem = _container.getChildAt(idx) as TabItem
            tabItem.setTitle(text)

            if (distributionMode == DistributionMode.FillEqually) {

                val alignment = if (forceCenterGravity) {
                    Gravity.CENTER_HORIZONTAL
                } else {
                    val firstElementAlignment: Int
                    val lastElementAlignment: Int

                    if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                        firstElementAlignment = Gravity.END
                        lastElementAlignment = Gravity.START
                    } else {
                        firstElementAlignment = Gravity.START
                        lastElementAlignment = Gravity.END
                    }

                    when (idx) {
                        0 -> firstElementAlignment
                        tabsCount -> lastElementAlignment
                        else -> Gravity.CENTER_HORIZONTAL
                    }
                }
                tabItem.setAlignment(alignment)
            }

            tabItem.setOnClickListener {
                selectedTab = idx
                _listener?.invoke(this, idx)
            }
        }

        if (selectedTab == -1) {
            selectedTab = 0
        }
    }

    fun setOnTabSelectedListener(listener: OnTabSelectedListener) {
        this._listener = listener
    }

    fun selectTab(idx: Int, animated: Boolean) {
        _container
            .children
            .forEachIndexed { index, tab ->
                val isSelected = index == idx

                tab.isSelected = isSelected

                if (isSelected) {
                    _container.doOnLayout {
                        _highlighter.highlight(tab.x, tab.width.toFloat(), animated)
                    }
                }
            }

        if (!_container.isInLayout) {
            _container.requestLayout()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, selectedTab)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)

        selectedTab = state.selectedTab
    }

    @Parcelize
    private data class SavedState(
        val state: Parcelable?,
        val selectedTab: Int,
    ) : BaseSavedState(state), Parcelable
}

class TabItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val animationDuration = resources
        .getInteger(android.R.integer.config_shortAnimTime)
        .toLong()

    private val txtTitle: AppCompatTextView

    init {
        val value = resources
            .getDimension(dimen.spacing_sm)
            .toInt()

        txtTitle = AppCompatTextView(context).also {
            it.setPadding(value)
            it.setTextAppearance(TextAppearance.Body2(Weight.SemiBold))
            it.setTextColor(ThemeColor.Content.Normal)

            it.textDirection = TEXT_DIRECTION_LOCALE
            it.textAlignment = TEXT_ALIGNMENT_CENTER
            it.gravity = Gravity.CENTER_HORIZONTAL

            val params = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            params.gravity = Gravity.CENTER

            addView(
                it,
                params
            )
        }
    }

    fun setTitle(title: String) {
        txtTitle.text = title
    }

    fun setAlignment(alignment: Int) {
        txtTitle.updateLayoutParams<LayoutParams> {
            gravity = alignment
        }
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

        val color = if (selected) {
            ThemeColor.Content.Normal
        } else {
            ThemeColor.Content.Opacity30
        }

        txtTitle.setTextColor(color)
    }
}

private class Highlighter @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    @get: ColorInt
    var underlineColor: Int
        get() = _selectionPaint.color
        set(value) {
            _selectionPaint.color = value
            invalidate()
        }

    var hasUnderline: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    private val _backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = context.resolveColor(ThemeColor.Content.Opacity10)
    }

    private val _selectionPaint = Paint().apply {
        isAntiAlias = true
        color = context.resolveColor(ThemeColor.Solid.Green)
    }

    private var _highlightedArea: HighlightedArea? = null

    init {
        hasUnderline = true
    }

    fun highlight(x: Float, width: Float, animated: Boolean) {
        _highlightedArea = HighlightedArea(x, width)
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val w = width.toFloat()

        if (hasUnderline) {
            canvas.drawPath(getRoundedRectPath(0f, w), _backgroundPaint)
        }

        _highlightedArea?.let {
            canvas.drawPath(getRoundedRectPath(it.offset, it.offset + it.width), _selectionPaint)
        }
    }

    private fun getRoundedRectPath(left: Float, w: Float): Path {
        val h = height.toFloat()
        val cornerRadii = h / 2

        val path = Path()

        path.addRoundRect(
            left,
            0f,
            w,
            h,
            cornerRadii,
            cornerRadii,
            Path.Direction.CW
        )

        return path
    }

    private data class HighlightedArea(val offset: Float, val width: Float)
}

private class TabsContainerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    var distributionMode: TabMenu.DistributionMode = TabMenu.DistributionMode.FillEqually
        set(value) {
            field = value
            requestLayout()
        }

    private val isRTL: Boolean

    init {
        layoutDirection = LAYOUT_DIRECTION_LOCALE

        val direction = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
        isRTL = direction == ViewCompat.LAYOUT_DIRECTION_RTL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var contentHeight = 0

        children.forEach {
            val width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            val height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

            it.measure(width, height)

            if (it.measuredHeight > contentHeight) {
                contentHeight = it.measuredHeight
            }
        }

        val resultWidth = MeasureSpec.getSize(widthMeasureSpec)
        val resultHeight = contentHeight

        setMeasuredDimension(
            resultWidth or MeasureSpec.EXACTLY,
            resultHeight or MeasureSpec.EXACTLY
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) {
            return
        }

        if (distributionMode == TabMenu.DistributionMode.FillEqually) {
            layoutAsFillEqually()
        } else {
            layoutAsSpaceBetween()
        }
    }

    private fun layoutAsSpaceBetween() {
        val contentWidth = children.sumOf { it.measuredWidth }
        val spacing = (measuredWidth - contentWidth) / (childCount - 1)

        var offset = 0

        children.forEachIndexed { index, v ->
            if (index == 0 && isRTL) {
                offset = width - v.measuredWidth
            }

            if (index > 0) {
                val previous = getChildAt(index - 1)

                offset = if (isRTL) {
                    previous.left - v.measuredWidth - spacing
                } else {
                    previous.right + spacing
                }
            }

            v.layout(offset, 0, offset + v.measuredWidth, height)
        }
    }

    private fun layoutAsFillEqually() {
        val widthPerTab = width / childCount

        var offset = 0

        children.forEachIndexed { index, v ->
            if (index == 0 && isRTL) {
                offset = width - widthPerTab
            }

            if (index > 0) {
                val previous = getChildAt(index - 1)

                offset = if (isRTL) {
                    previous.left - widthPerTab
                } else {
                    widthPerTab + previous.left
                }
            }

            v.layout(offset, 0, offset + widthPerTab, height)
        }
    }
}