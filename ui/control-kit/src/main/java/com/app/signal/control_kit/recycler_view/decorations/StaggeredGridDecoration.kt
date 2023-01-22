package com.app.signal.control_kit.recycler_view.decorations

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class StaggeredGridDecoration(
    ctx: Context,
    private val numberOfColumns: Int,
    @DimenRes rowSpacingRes: Int,
    @DimenRes columnSpacingRes: Int
) : RecyclerView.ItemDecoration() {
    private val rowSpacing: Int
    private val columnSpacing: Int

    init {
        this.rowSpacing = ctx.resources.getDimension(rowSpacingRes).toInt()
        this.columnSpacing = ctx.resources.getDimension(columnSpacingRes).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        val numberOfRows = itemCount / numberOfColumns
        val column = position % numberOfColumns
        val row = position / numberOfColumns

        if (column < numberOfColumns - 1) {
            outRect.bottom = rowSpacing
        }

        if (row < numberOfRows) {
            outRect.right = columnSpacing
        }
    }
}