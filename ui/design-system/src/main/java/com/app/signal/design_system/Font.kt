package com.app.signal.design_system

import androidx.annotation.AttrRes

enum class Weight {
    Regular, SemiBold
}

sealed class TextAppearance(val weight: Weight) {
    class H1(weight: Weight = Weight.Regular): TextAppearance(weight) {
        override val attrResId: Int
            get() = when (weight) {
                Weight.Regular -> R.attr.textAppearanceH1_Regular
                Weight.SemiBold -> R.attr.textAppearanceH1_SemiBold
            }
        override val fontSize: Float = 64f
    }

    class H2(weight: Weight = Weight.Regular): TextAppearance(weight) {
        override val attrResId: Int
            get() = when (weight) {
                Weight.Regular -> R.attr.textAppearanceH2_Regular
                Weight.SemiBold -> R.attr.textAppearanceH2_SemiBold
            }

        override val fontSize: Float = 28f
    }

    class H3(weight: Weight = Weight.Regular): TextAppearance(weight) {
        override val attrResId: Int
            get() = when (weight) {
                Weight.Regular -> R.attr.textAppearanceH3_Regular
                Weight.SemiBold -> R.attr.textAppearanceH3_SemiBold
            }

        override val fontSize: Float = 24f
    }

    class H4(weight: Weight = Weight.Regular): TextAppearance(weight) {
        override val attrResId: Int
            get() = when (weight) {
                Weight.Regular -> R.attr.textAppearanceH4_Regular
                Weight.SemiBold -> R.attr.textAppearanceH4_SemiBold
            }

        override val fontSize: Float = 20f
    }

    class Body1(weight: Weight = Weight.Regular): TextAppearance(weight) {
        override val attrResId: Int
            get() = when (weight) {
                Weight.Regular -> R.attr.textAppearanceBody1_Regular
                Weight.SemiBold -> R.attr.textAppearanceBody1_SemiBold
            }

        override val fontSize: Float = 16f
    }

    class Body2(weight: Weight = Weight.Regular): TextAppearance(weight) {
        override val attrResId: Int
            get() = when (weight) {
                Weight.Regular -> R.attr.textAppearanceBody2_Regular
                Weight.SemiBold -> R.attr.textAppearanceBody2_SemiBold
            }

        override val fontSize: Float = 14f
    }

    class Footnote(weight: Weight = Weight.Regular): TextAppearance(weight) {
        override val attrResId: Int
            get() = when (weight) {
                Weight.Regular -> R.attr.textAppearanceFootnote_Regular
                Weight.SemiBold -> R.attr.textAppearanceFootnote_SemiBold
            }

        override val fontSize: Float = 12f
    }

    @get: AttrRes
    abstract val attrResId: Int
    abstract val fontSize: Float

    val isSemiBold: Boolean get() {
        return weight == Weight.SemiBold
    }
}