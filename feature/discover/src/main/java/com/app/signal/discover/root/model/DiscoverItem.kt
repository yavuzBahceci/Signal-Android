package com.app.signal.discover.root.model

import androidx.recyclerview.widget.DiffUtil
import com.app.signal.domain.model.photo.Image
import kotlinx.coroutines.flow.MutableSharedFlow

sealed class DiscoverItem(open val id: String) {

    data class Photo(
        override val id: String,
        val title: String,
        val image: Image?,
        val actionFlow: MutableSharedFlow<DiscoverAction>,
    ) : DiscoverItem("PHOTO_$id")


    companion object {
        val DIFF = object : DiffUtil.ItemCallback<DiscoverItem>() {
            override fun areItemsTheSame(
                oldItem: DiscoverItem,
                newItem: DiscoverItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DiscoverItem,
                newItem: DiscoverItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}