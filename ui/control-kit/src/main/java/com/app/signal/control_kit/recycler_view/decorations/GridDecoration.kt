package com.app.signal.control_kit.recycler_view.decorations

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class GridDecoration(
    ctx: Context,
    private val spanCount: Int,
    @DimenRes rowSpacingRes: Int,
    @DimenRes columnSpacingRes: Int
) : RecyclerView.ItemDecoration() {
    private val colSpacing: Int
    private val rowSpacing: Int

    init {
        this.colSpacing = ctx.resources.getDimension(rowSpacingRes).toInt()
        this.rowSpacing = ctx.resources.getDimension(columnSpacingRes).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        val numberOfRows = itemCount / spanCount
        val row = position / spanCount

        outRect.top = colSpacing

        if (row < numberOfRows) {
            outRect.right = rowSpacing
        }
    }
}