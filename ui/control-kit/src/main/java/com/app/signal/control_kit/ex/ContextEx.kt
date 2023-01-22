package com.app.signal.control_kit.ex

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.app.signal.design_system.ColorProvider

@ColorInt
fun Context.resolveColor(color: ColorProvider): Int {
    return ResourcesCompat.getColor(resources, color.resId, null)
}