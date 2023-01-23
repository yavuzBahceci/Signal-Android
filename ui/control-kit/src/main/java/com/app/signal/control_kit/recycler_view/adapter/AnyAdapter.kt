package com.app.signal.control_kit.recycler_view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.BuildConfig

abstract class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    open fun bind(item: T) {}

    open fun onRecycle() {}
    open fun onDetached() {}
}

typealias ViewHolderFactory<T> = (View) -> ViewHolder<T>

abstract class AnyAdapter<T>(diff: DiffUtil.ItemCallback<T>): RecyclerView.Adapter<ViewHolder<T>>() {
    private val differ by lazy { AsyncListDiffer(this, diff) }
    private val factories by lazy { mutableMapOf<Int, Factory<T>>() }

    abstract fun getItemViewType(model: T): Int

    open fun onBindViewHolder(holder: ViewHolder<T>, model: T) {
        holder.bind(model)
    }

    open fun onBindViewHolder(holder: ViewHolder<T>, model: T, position: Int) {
        onBindViewHolder(holder, model)
    }

    fun bind(viewType: Int, @LayoutRes layoutId: Int, factory: (View) -> ViewHolder<out T>) {
        bind(viewType, Factory(layoutId, factory))
    }

    fun bind(@LayoutRes layoutId: Int, factory: (View) -> ViewHolder<out T>) {
        bind(layoutId, Factory(layoutId, factory))
    }

    fun submit(item: T?, callback: (() -> Unit)? = null) {
        val list = if (item != null) listOf(item) else emptyList()
        differ.submitList(list, callback)
    }

    fun submit(list: List<T>, callback: (() -> Unit)? = null) {
        differ.submitList(list, callback)
    }

    fun getItem(position: Int): T {
        return differ.currentList[position]
    }

    fun tryGetItem(position: Int): T? {
        val list = differ.currentList
        return if (position < list.size) list[position] else null
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return getItemViewType(differ.currentList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val timestamp = System.currentTimeMillis()

        val binding = factories[viewType] ?: throw Exception("no binding for view type $viewType")

        val view = LayoutInflater
            .from(parent.context)
            .inflate(binding.layoutId, parent, false)

        if (BuildConfig.DEBUG) {
            val result = System.currentTimeMillis() - timestamp
            Log.d("PERFORMANCE", "VIEW_HOLDER ${view::class.java.canonicalName} = $result ms")
        }

        @Suppress("UNCHECKED_CAST")
        return binding.factory(view) as ViewHolder<T>
    }

    override fun onViewRecycled(holder: ViewHolder<T>) {
        super.onViewRecycled(holder)
        holder.onRecycle()
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        onBindViewHolder(holder, differ.currentList[position], position)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetached()
    }

    private fun bind(viewType: Int, factory: Factory<T>) {
        factories[viewType] = factory
    }

    private data class Factory<T>(
        @LayoutRes val layoutId: Int,
        val factory: ViewHolderFactory<out T>,
    )
}