package com.app.signal.control_kit

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.updateLayoutParams

class SegmentedProgressBar: LinearLayoutCompat {
    var segments: Int = 3
        set(value) {
            field = value
            setupSegments()
        }

    var progress: Double = 0.0
        set(value) {
            field = value
            updateSegmentsProgress()
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = HORIZONTAL
    }

    private fun setupSegments() {
        val difference = segments - childCount

        if (difference <= 0) {
            return
        }

        for (i in 0 until difference) {
            addView(SegmentProgressView(context))
        }

        val weight = 1f / childCount

        for (i in 0 until childCount) {
            getChildAt(i).updateLayoutParams {
                width = 0
                height = LayoutParams.MATCH_PARENT
                weightSum = weight
            }
        }

        updateSegmentsAppearance()
    }

    private fun updateSegmentsProgress() {
        if (childCount == 0) {
            return
        }

        val fillProgressPerSegment: Double = 1.0 / childCount
        var tmp = progress

//        arrangedSubviews
//            .compactMap { $0 as? SegmentView }
//            .forEach { segment in
//                var value: CGFloat
//
//                if tmp >= fillProgressPerSegment {
//                    value = fillProgressPerSegment
//                    tmp -= fillProgressPerSegment
//                } else {
//                    value = tmp
//                    tmp = 0
//                }
//
//                segment.progress = 1 * value / fillProgressPerSegment
//            }
    }

    private fun updateSegmentsAppearance() {
        for (i in 0..childCount) {
            //                $0.layer.cornerRadius = segmentCornerRadius
//
//                $0.backgroundColor = segmentBackgroundColor
//                $0.progressColor = segmentProgressColor
        }
    }
}

private class SegmentProgressView: FrameLayout {
    var progress: Double = 0.0
        set(value) {
            field = value
            updateProgressView()
        }

//    private lateinit var progressView: View

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup()
    }

    private fun setup() {
//        progressView = View(context).also {
//            it.setBackgroundAttrColor(R.attr.colorSolidGreen)
//            addView(it)
//        }
    }

    private fun updateProgressView() {
        val progress = (width * progress).toInt()

//        progressView.layout(0, 0, progress, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        updateProgressView()
    }
}
