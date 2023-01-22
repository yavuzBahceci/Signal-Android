package com.app.signal.control_kit.ex

import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.widget.TextViewCompat
import com.app.signal.design_system.ColorProvider
import com.app.signal.design_system.TextAppearance
import resolveTextAppearanceResId

fun TextView.setHighlightColor(color: ColorProvider) {
    highlightColor = context.resolveColor(color)
}


fun TextView.setTextColor(color: ColorProvider) {
    setTextColor(context.resolveColor(color))
}

fun TextView.setTextAppearance(appearance: TextAppearance) {
    TextViewCompat.setTextAppearance(
        this,
        context.resolveTextAppearanceResId(appearance.attrResId)
    )
}

fun TextView.setAttrTextAppearance(@AttrRes attResId: Int) {
    TextViewCompat.setTextAppearance(
        this,
        context.resolveTextAppearanceResId(attResId)
    )
}

