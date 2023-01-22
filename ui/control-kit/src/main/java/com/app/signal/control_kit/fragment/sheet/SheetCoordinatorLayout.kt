package com.app.signal.control_kit.fragment.sheet

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.app.signal.control_kit.R
import com.app.signal.control_kit.ex.resolveColor
import com.app.signal.design_system.R.*
import com.app.signal.design_system.ThemeColor
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface SheetAnimator {
    fun getSheetTranslationY(): Float
    fun setSheetTranslationY(value: Float)
}

class SheetCoordinatorLayout : CoordinatorLayout, SheetAnimator {
    var fitToContent: Boolean
        set(value) {
            field = value
            reflectFitToContent()
        }

    var expanded: Boolean
        set(value) {
            field = value
            behavior.state = if (field) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        }

    val behavior: BottomSheetBehavior<FrameLayout>

    private val sheet: FrameLayout

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setBackgroundColor(context.resolveColor(ThemeColor.Sheet.Foreground))

        sheet = FrameLayout(context, null, com.google.android.material.R.attr.bottomSheetStyle).also {
            it.isDuplicateParentStateEnabled = false
            it.clipToPadding = false
            it.background = ContextCompat.getDrawable(
                context,
                drawable.shape_sheet_content_bg
            )
        }

        addView(sheet)

        sheet.updateLayoutParams<LayoutParams> {
            width = LayoutParams.MATCH_PARENT
            height = LayoutParams.WRAP_CONTENT
            behavior = BottomSheetBehavior<FrameLayout>(context, null)
        }

        behavior = BottomSheetBehavior.from(sheet).also {
            it.isHideable = true
        }

        context
            .theme.obtainStyledAttributes(attrs, R.styleable.SheetCoordinatorLayout, 0, 0)
            .also {
                fitToContent = it.getBoolean(R.styleable.SheetCoordinatorLayout_fitToContent, true)
                expanded = it.getBoolean(R.styleable.SheetCoordinatorLayout_expanded, false)
                it.recycle()
            }
    }

    private fun reflectFitToContent() {
        sheet.updateLayoutParams<LayoutParams> {
            height = if (fitToContent) {
                LayoutParams.WRAP_CONTENT
            } else {
                LayoutParams.MATCH_PARENT
            }
        }

        behavior.isFitToContents = fitToContent
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child == sheet) {
            super.addView(child, index, params)
        } else {
            sheet.addView(child, params)
        }
    }

    override fun getSheetTranslationY(): Float {
        return sheet.let {
            (it.height - it.translationY) / it.height
        }
    }

    override fun setSheetTranslationY(value: Float) {
        sheet.apply {
            translationY = height - height * value
        }
    }
}