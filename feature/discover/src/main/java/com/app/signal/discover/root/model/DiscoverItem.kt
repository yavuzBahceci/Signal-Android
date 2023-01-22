package com.app.signal.discover.root.model

import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import com.app.signal.domain.model.photo.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

sealed class DiscoverItem(val type: String) {
    val stableId: Long = type.hashCode().toLong()

    data class Header(
        @StringRes val titleRes: Int,
    ) : DiscoverItem("HEADER")

    data class Photo(
        val id: String,
        val title: String,
        val image: Image?,
        val actionFlow: MutableSharedFlow<DiscoverAction>,
        val scope: CoroutineScope
    ) : DiscoverItem("PHOTO_$id")


    companion object {
        val DIFF = object : DiffUtil.ItemCallback<DiscoverItem>() {
            override fun areItemsTheSame(
                oldItem: DiscoverItem,
                newItem: DiscoverItem
            ): Boolean {
                return oldItem.stableId == newItem.stableId
            }

            override fun areContentsTheSame(
                oldItem: DiscoverItem,
                newItem: DiscoverItem
            ): Boolean {
                return oldItem.stableId == newItem.stableId
            }
        }
    }
}