package com.app.signal.control_kit

import android.content.Context
import android.util.AttributeSet
import com.app.signal.control_kit.ex.setTextAppearance
import com.app.signal.control_kit.ex.setTextColor
import com.app.signal.design_system.TextAppearance
import com.app.signal.design_system.ThemeColor
import com.app.signal.design_system.Weight

class MenuItemButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextButton(context, attrs) {
    init {
        setTextAppearance(TextAppearance.Body2(Weight.SemiBold))
        setTextColor(ThemeColor.Content.Normal)

        val spacing = resources
            .getDimension(com.app.signal.design_system.R.dimen.spacing_md)
            .toInt()

        setPadding(spacing, 0, spacing, 0)
    }
}