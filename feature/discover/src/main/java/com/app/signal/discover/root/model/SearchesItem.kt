package com.app.signal.discover.root.model

import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.MutableSharedFlow

data class SearchItem(
    val text: String,
    val actionFlow: MutableSharedFlow<DiscoverAction>
) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(
                oldItem: SearchItem,
                newItem: SearchItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SearchItem,
                newItem: SearchItem
            ): Boolean {
                return oldItem.text== newItem.text
            }
        }
    }
}