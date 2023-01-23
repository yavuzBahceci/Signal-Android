package com.app.saved.root.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.app.saved.root.model.SavedAction
import com.app.saved.root.model.SavedItem
import com.app.signal.control_kit.CircularImageView
import com.app.signal.control_kit.recycler_view.adapter.StatefulAdapter
import com.app.signal.control_kit.recycler_view.adapter.ViewHolder
import com.app.signal.saved.R

internal class SavedAdapter : StatefulAdapter<SavedItem>(SavedItem.DIFF) {
    init {
        bind(
            R.layout.saved_item, ::RowHolder
        )
    }

    override fun getItemViewType(model: SavedItem) = R.layout.saved_item
}


private class RowHolder(itemView: View) : ViewHolder<SavedItem.Photo>(itemView) {

    private val imgLogo: CircularImageView = itemView.findViewById(R.id.img_thumbnail)
    private val txtId: TextView = itemView.findViewById(R.id.txt_image_id)
    private val txtTitle: TextView = itemView.findViewById(R.id.txt_image_title)
    private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

    @SuppressLint("NewApi")
    override fun bind(item: SavedItem.Photo) {

        itemView.setOnClickListener {
            item.actionFlow.tryEmit(SavedAction.Select(item))
        }

        deleteButton.setOnClickListener {
            item.actionFlow.tryEmit(SavedAction.Delete(item))
        }

        imgLogo.loadImage(item.image?.thumbNailUrl)

        txtId.text = item.id
        txtTitle.text = item.title

    }
}