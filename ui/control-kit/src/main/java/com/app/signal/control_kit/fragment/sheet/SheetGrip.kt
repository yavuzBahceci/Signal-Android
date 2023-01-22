package com.app.signal.control_kit.fragment.sheet

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.app.signal.control_kit.R

class SheetGrip @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val defaultWidth: Int = resources.getDimension(R.dimen.sheet_grip_width).toInt()
    private val defaultHeight: Int = resources.getDimension(R.dimen.sheet_grip_height).toInt()

    init {
        setBackgroundResource(R.drawable.sheet_grip_shape)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val resultWidth = if (layoutParams.width == WRAP_CONTENT) {
            defaultWidth
        } else {
            MeasureSpec.getSize(widthMeasureSpec)
        }

        val resultHeight = if (layoutParams.height == WRAP_CONTENT) {
            defaultHeight
        } else {
            MeasureSpec.getSize(heightMeasureSpec)
        }

        setMeasuredDimension(
            resultWidth or MeasureSpec.EXACTLY,
            resultHeight or MeasureSpec.EXACTLY
        )
    }
}