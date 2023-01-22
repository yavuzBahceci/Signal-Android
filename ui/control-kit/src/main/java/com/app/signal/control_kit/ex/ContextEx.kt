package com.app.signal.control_kit.ex

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.app.signal.design_system.ColorProvider
import com.app.signal.design_system.TextAppearance

@ColorInt
fun Context.resolveColor(color: ColorProvider): Int {
    return ResourcesCompat.getColor(resources, color.resId, null)
}

@ColorInt
fun Context.resolveColor(@ColorRes resId: Int): Int {
    return ResourcesCompat.getColor(resources, resId, null)
}

data class Font(
    val appearanceId: Int,
    val typeface: Typeface,
    val size: Float,
    val scaledSize: Float,
)

fun Context.resolveFont(appearance: TextAppearance): Font {
    val typeface: Typeface

    val fontResId: Int
    val style: Int

    if (appearance.isSemiBold) {
        fontResId = com.app.signal.design_system.R.font.proxima_nova_semibold
        style = Typeface.BOLD
    } else {
        fontResId = com.app.signal.design_system.R.font.proxima_nova
        style = Typeface.NORMAL
    }

    val density = resources.displayMetrics.scaledDensity
    val scaleFontSize: Float = appearance.fontSize * density

    typeface = Typeface.create(
        ResourcesCompat.getFont(this, fontResId),
        style
    )

    return Font(
        appearance.attrResId,
        typeface,
        appearance.fontSize,
        scaleFontSize
    )
}
