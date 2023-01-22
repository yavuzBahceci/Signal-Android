package com.app.saved.root.model

import androidx.recyclerview.widget.DiffUtil
import com.app.signal.domain.model.photo.Image
import kotlinx.coroutines.flow.MutableSharedFlow

sealed class SavedItem(open val id: String) {

    data class Photo(
        override val id: String,
        val title: String,
        val image: Image?,
        val actionFlow: MutableSharedFlow<SavedAction>,
    ) : SavedItem("PHOTO_$id")


    companion object {
        val DIFF = object : DiffUtil.ItemCallback<SavedItem>() {
            override fun areItemsTheSame(
                oldItem: SavedItem,
                newItem: SavedItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SavedItem,
                newItem: SavedItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}