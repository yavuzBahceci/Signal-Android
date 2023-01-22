package com.app.signal.discover.root

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.app.signal.control_kit.CircularImageView
import com.app.signal.control_kit.recycler_view.adapter.AnyAdapter
import com.app.signal.control_kit.recycler_view.adapter.ViewHolder
import com.app.signal.discover.R
import com.app.signal.discover.root.model.DiscoverAction
import com.app.signal.discover.root.model.DiscoverItem

internal class DiscoverAdapter : AnyAdapter<DiscoverItem>(DiscoverItem.DIFF) {
    init {
        bind(
            R.layout.discover_header_item, ::HeaderHolder
        )
        bind(
            R.layout.discover_photo_item, ::RowHolder
        )
    }

    override fun getItemViewType(model: DiscoverItem) = when (model) {
        is DiscoverItem.Header -> R.layout.discover_header_item
        is DiscoverItem.Photo -> R.layout.discover_photo_item
    }
}

private class HeaderHolder(itemView: View) :
    ViewHolder<DiscoverItem.Header>(itemView) {
    private val txtTitle: AppCompatTextView =
        itemView.findViewById(R.id.txt_title)

    override fun bind(item: DiscoverItem.Header) {
        txtTitle.setText(item.titleRes)
    }
}


private class RowHolder(itemView: View) : ViewHolder<DiscoverItem.Photo>(itemView) {

    private val imgLogo: CircularImageView = itemView.findViewById(R.id.img_thumbnail)
    private val txtId: TextView = itemView.findViewById(R.id.txt_image_id)
    private val txtTitle: TextView = itemView.findViewById(R.id.txt_image_title)

    override fun bind(item: DiscoverItem.Photo) {

        itemView.setOnClickListener {
            item.actionFlow.tryEmit(DiscoverAction.Select(item))
        }

        imgLogo.loadImage(item.image?.thumbNailUrl)

        txtId.text = item.id
        txtTitle.text = item.title

    }
}