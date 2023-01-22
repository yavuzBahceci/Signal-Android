package com.app.signal.design_system

import androidx.annotation.ColorRes

interface ColorProvider {
    @get: ColorRes
    val resId: Int
}

object ThemeColor {
    enum class Background: ColorProvider {
        Primary,
        Secondary,
        Tertiary;

        override val resId: Int get() = when (this) {
            Primary -> R.color.background_primary
            Secondary -> R.color.background_secondary
            Tertiary -> R.color.background_tertiary
        }
    }

    enum class Solid: ColorProvider {
        White, Red, Yellow, Green, GreenOpacity10, Transparent;

        override val resId: Int get() = when (this) {
            White -> R.color.solid_white
            Red -> R.color.solid_red
            Yellow -> R.color.solid_yellow
            Green -> R.color.solid_green
            GreenOpacity10 -> R.color.solid_green_10
            Transparent -> android.R.color.transparent
        }
    }

    enum class Content: ColorProvider {
        Normal, Opacity50, Opacity30, Opacity10;

        override val resId: Int get() = when (this) {
            Normal -> R.color.content
            Opacity50 -> R.color.content_50
            Opacity30 -> R.color.content_30
            Opacity10 -> R.color.content_10
        }
    }

    enum class Sheet: ColorProvider {
        Foreground, Content;

        override val resId: Int get() = when (this) {
            Foreground -> R.color.sheet_foreground_color
            Content -> R.color.sheet_content_bg_color
        }
    }

    enum class SystemMenu: ColorProvider {
        Background, Border, Fallback;

        override val resId: Int get() = when (this) {
            Background -> R.color.system_menu_background
            Border -> R.color.system_menu_border
            Fallback -> R.color.system_menu_fallback
        }
    }
}