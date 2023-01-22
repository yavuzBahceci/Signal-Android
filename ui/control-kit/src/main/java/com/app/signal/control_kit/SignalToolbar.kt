package com.app.signal.control_kit

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.signal.control_kit.ex.resolveColor
import com.app.signal.design_system.R.*
import com.app.signal.design_system.ThemeColor
import com.google.android.material.appbar.MaterialToolbar
import resolveTextAppearanceResId

class SignalToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    lateinit var primaryToolbar: MaterialToolbar
    lateinit var secondaryToolbar: MaterialToolbar

    private lateinit var txtPrimaryTitle: TextView
    private lateinit var txtSecondaryTitle: TextView
    private lateinit var txtSecondarySubtitle: TextView

    var smallTextAppearance: Int? = null
    var mediumTextAppearance: Int? = null

    var grayTextColor: Int? = null
    var contentTextColor: Int? = null
    var hasBottomLine: Boolean = true

    private lateinit var toolbarBottomLine: View

    init {
        inflateLayout()
        applyAttributes(attrs)
    }

    private fun inflateLayout() {
        val content = View.inflate(context, R.layout.signal_toolbar, this)

        txtPrimaryTitle = content.findViewById(R.id.primary_title)
        primaryToolbar = content.findViewById(R.id.primary_toolbar)
        secondaryToolbar = content.findViewById(R.id.secondary_toolbar)
        toolbarBottomLine = content.findViewById(R.id.secondary_toolbar_bottom_line)

        primaryToolbar.title = ""
        smallTextAppearance = context.resolveTextAppearanceResId(
            attr.textAppearanceBody2_Regular
        )
        mediumTextAppearance = context.resolveTextAppearanceResId(
            attr.textAppearanceBody1_SemiBold
        )
        grayTextColor = context.resolveColor(ThemeColor.Content.Opacity50)
        contentTextColor = context.resolveColor(ThemeColor.Content.Normal)

    }

    private fun applyAttributes(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SignalToolbar)

        val title = a.getString(R.styleable.SignalToolbar_title)
        val hasSubtitle = a.getBoolean(R.styleable.SignalToolbar_hasSubtitle, false)
        hasBottomLine = a.getBoolean(R.styleable.SignalToolbar_hasBottomLine, true)

        if (hasSubtitle) {
            smallTextAppearance?.let { secondaryToolbar.setTitleTextAppearance(context, it) }
            mediumTextAppearance?.let { secondaryToolbar.setSubtitleTextAppearance(context, it) }
            contentTextColor?.let { secondaryToolbar.setSubtitleTextColor(it) }
            grayTextColor?.let { secondaryToolbar.setTitleTextColor(it) }
        }
        txtPrimaryTitle.text = title
        secondaryToolbar.title = title
        a.recycle()
    }

    fun updateToolbarProgress(progressPercentage: Float) {
        if (progressPercentage < 1f) {
            txtPrimaryTitle.alpha = 1f - progressPercentage
            secondaryToolbar.alpha = progressPercentage
            if (hasBottomLine) toolbarBottomLine.alpha = progressPercentage
        } else {
            txtPrimaryTitle.alpha = 0f
            secondaryToolbar.alpha = 1f
            if (hasBottomLine) toolbarBottomLine.alpha = 1f
        }
    }

    fun setSubtitle(subTitle: String) {
        secondaryToolbar.subtitle = subTitle
    }

    fun setSecondaryTitle(title: String) {
        secondaryToolbar.title = title
    }

    val Float.toPx: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        ).toInt()
}