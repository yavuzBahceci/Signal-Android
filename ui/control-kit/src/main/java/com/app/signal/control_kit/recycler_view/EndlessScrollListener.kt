package com.app.signal.control_kit.recycler_view

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {
    private var visibleThreshold = 6
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 0

    private var mLayoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager) {
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }


    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount

        when (val manager = mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = manager.findLastVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                lastVisibleItemPosition = manager.findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItemPosition = manager.findLastVisibleItemPosition()
            }
        }

        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    fun reset() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
}