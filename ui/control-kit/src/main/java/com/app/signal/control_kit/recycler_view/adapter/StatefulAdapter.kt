package com.app.signal.control_kit.recycler_view.adapter

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.signal.control_kit.ex.findViewByType

abstract class StatefulAdapter<T>(diff: DiffUtil.ItemCallback<T>) : AnyAdapter<T>(diff) {
    private var scrollState: Parcelable? = null
    private val listener: RecyclerView.OnScrollListener

    val getAdapterKey: String get() {
        return "ADAPTER_${this.javaClass.canonicalName}"
    }

    init {
        listener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrollState = recyclerView.layoutManager?.onSaveInstanceState()
                }
            }
        }
    }

    fun getAdapterState(): Parcelable? {
       return scrollState
    }

    fun restoreAdapterState(state: Parcelable?) {
        this.scrollState = state
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, model: T) {
        super.onBindViewHolder(holder, model)

        val rv = holder.itemView.findViewByType(RecyclerView::class) ?: return

        if (scrollState != null) {
            rv.layoutManager?.onRestoreInstanceState(scrollState)
        }

        rv.addOnScrollListener(listener)
    }

    override fun onViewRecycled(holder: ViewHolder<T>) {
        super.onViewRecycled(holder)

        val rv = holder.itemView.findViewByType(RecyclerView::class) ?: return
        rv.removeOnScrollListener(listener)
    }
}