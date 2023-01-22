package com.app.signal.control_kit.recycler_view.decorations

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import getChildViewType

class SpacingDecoration : RecyclerView.ItemDecoration {
    private val viewTypes: List<Int>?
    private val spacing: Int

    constructor(viewType: Int, ctx: Context, @DimenRes spacingRes: Int) : this(
        listOf(viewType),
        ctx,
        spacingRes
    )

    constructor(viewTypes: List<Int>? = null, ctx: Context, @DimenRes spacingRes: Int) : super() {
        this.viewTypes = viewTypes
        this.spacing = ctx.resources.getDimension(spacingRes).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (viewTypes != null) {
            val type = getChildViewType(parent, view)

            if (!viewTypes.contains(type)) {
                return
            }
        }


        val position = parent.getChildAdapterPosition(view)
        val count = parent.adapter?.itemCount ?: 0

        val orientation = when (val layoutManager = parent.layoutManager) {
            is LinearLayoutManager -> layoutManager.orientation
            else -> RecyclerView.HORIZONTAL
        }

        if (position < count - 1) {
            if (orientation == RecyclerView.HORIZONTAL) {
                outRect.set(0, 0, spacing, 0)
            } else {
                outRect.set(0, 0, 0, spacing)
            }
        }
    }
}