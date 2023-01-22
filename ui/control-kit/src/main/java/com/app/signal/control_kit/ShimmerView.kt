package com.app.signal.control_kit

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout

class ShimmerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShimmerFrameLayout(context, attrs, defStyleAttr) {
    init {
        val shimmer = Shimmer
            .AlphaHighlightBuilder()
            .setClipToChildren(true)
            .setBaseAlpha(0.6f)
            .setHighlightAlpha(1f)
            .setTilt(0f)
            .build()

        setShimmer(shimmer)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}