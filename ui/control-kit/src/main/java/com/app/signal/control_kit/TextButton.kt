package com.app.signal.control_kit

import android.content.Context
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.app.signal.control_kit.ex.resolveColor
import com.app.signal.design_system.TextAppearance
import com.app.signal.design_system.ThemeColor
import com.app.signal.design_system.Weight
import resolveTextAppearanceResId

open class TextButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {
    private val animationDuration = resources
        .getInteger(android.R.integer.config_shortAnimTime)
        .toLong()

    init {
        setup()
        applyAttributes(attrs)
    }

    private fun setup() {
        maxLines = 1
        textDirection = TEXT_DIRECTION_LOCALE
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        val fallbackTextColorResId = context.resolveColor(
            ThemeColor.Content.Opacity30
        )

        val fallbackTextAppearanceResId = context.resolveTextAppearanceResId(
            TextAppearance.Footnote(Weight.SemiBold).attrResId
        )

        val attributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TextButton,
            0,
            0
        )

        val textAppearanceId = attributes.getResourceId(
            R.styleable.TextButton_android_textAppearance,
            fallbackTextAppearanceResId
        )

        TextViewCompat.setTextAppearance(this, textAppearanceId)

        val textColor = attributes.getColor(
            R.styleable.TextButton_android_textColor,
            fallbackTextColorResId
        )

        setTextColor(textColor)

        attributes.recycle()
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)

        val opacity: Float = if (pressed) {
            0.3f
        } else {
            1f
        }

        animate()
            .alpha(opacity)
            .setInterpolator(DecelerateInterpolator())
            .setDuration(animationDuration)
            .start()
    }
}