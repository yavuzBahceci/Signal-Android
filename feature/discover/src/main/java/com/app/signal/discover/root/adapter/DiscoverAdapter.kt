package com.app.signal.discover.root.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.app.signal.control_kit.CircularImageView
import com.app.signal.control_kit.recycler_view.adapter.StatefulAdapter
import com.app.signal.control_kit.recycler_view.adapter.ViewHolder
import com.app.signal.discover.R
import com.app.signal.discover.root.model.DiscoverAction
import com.app.signal.discover.root.model.DiscoverItem

internal class DiscoverAdapter : StatefulAdapter<DiscoverItem>(DiscoverItem.DIFF) {
    init {
        bind(
            R.layout.discover_photo_item, ::RowHolder
        )
    }

    override fun getItemViewType(model: DiscoverItem) = R.layout.discover_photo_item
}


private class RowHolder(itemView: View) : ViewHolder<DiscoverItem.Photo>(itemView) {

    private val imgLogo: CircularImageView = itemView.findViewById(R.id.img_thumbnail)
    private val txtId: TextView = itemView.findViewById(R.id.txt_image_id)
    private val txtTitle: TextView = itemView.findViewById(R.id.txt_image_title)
    private val addButton: ImageView = itemView.findViewById(R.id.add_button)

    override fun bind(item: DiscoverItem.Photo) {

        itemView.setOnClickListener {
            item.actionFlow.tryEmit(DiscoverAction.Select(item))
        }

        addButton.setOnClickListener {
            item.actionFlow.tryEmit(DiscoverAction.Save(item))
        }

        imgLogo.loadImage(item.image?.thumbNailUrl)

        txtId.text = item.id
        txtTitle.text = item.title

    }
}