package com.app.signal.control_kit.recycler_view.decorations

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.graphics.Insets
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import getChildViewType

open class MarginDecoration: RecyclerView.ItemDecoration {
    private val viewTypes: List<Int>?
    private val excludedViewTypes: List<Int>?
    private val insets: Insets

    constructor(
        viewTypes: List<Int>? = null,
        excludedViewTypes: List<Int>? = null,
        ctx: Context,
        @DimenRes verticalResId: Int? = null,
        @DimenRes horizontalResId: Int? = null
    ): super() {
        this.viewTypes = viewTypes
        this.excludedViewTypes = excludedViewTypes

        val vertical = if (verticalResId != null) {
            ctx.resources.getDimension(verticalResId).toInt()
        } else {
            0
        }

        val horizontal = if (horizontalResId != null) {
            ctx.resources.getDimension(horizontalResId).toInt()
        } else {
            0
        }

        this.insets = Insets.of(horizontal, vertical, horizontal, vertical)
    }

    constructor(
        viewTypes: List<Int>? = null,
        excludedViewTypes: List<Int>? = null,
        ctx: Context,
        @DimenRes topResId: Int? = null,
        @DimenRes bottomResId: Int? = null,
        @DimenRes startResId: Int? = null,
        @DimenRes endResId: Int? = null,
    ): super() {
        this.viewTypes = viewTypes
        this.excludedViewTypes = excludedViewTypes

        val top = if (topResId != null) {
            ctx.resources.getDimension(topResId).toInt()
        } else {
            0
        }

        val bottom = if (bottomResId != null) {
            ctx.resources.getDimension(bottomResId).toInt()
        } else {
            0
        }

        val start = if (startResId != null) {
            ctx.resources.getDimension(startResId).toInt()
        } else {
            0
        }

        val end = if (endResId != null) {
            ctx.resources.getDimension(endResId).toInt()
        } else {
            0
        }

        this.insets = Insets.of(start, top, end, bottom)
    }

    constructor(viewTypes: List<Int>? = null, excludedViewTypes: List<Int>?, insets: Insets): super() {
        this.viewTypes = viewTypes
        this.excludedViewTypes = excludedViewTypes
        this.insets = insets
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val type = getChildViewType(parent, view)

        if (viewTypes != null) {
            if (!viewTypes.contains(type)) {
                return
            }
        }

        if (excludedViewTypes != null) {
            if (excludedViewTypes.contains(type)) {
                return
            }
        }

        var right = insets.right
        var left = insets.left

        val layoutManager = parent.layoutManager

        if (layoutManager is GridLayoutManager) {
            val position = parent.getChildLayoutPosition(view)

            val spanCount = layoutManager.spanCount
            val spanSize = layoutManager.spanSizeLookup.getSpanSize(position)

            if (spanSize < spanCount) {
                val column = position % spanCount

                left -= column * left / spanCount
                right = (column + 1) * right / spanCount
            }
        }

        outRect.set(
            left,
            insets.top,
            right,
            insets.bottom
        )
    }
}