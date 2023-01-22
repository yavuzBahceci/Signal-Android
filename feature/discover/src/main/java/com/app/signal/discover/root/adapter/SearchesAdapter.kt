package com.app.signal.discover.root.adapter

import android.view.View
import android.widget.TextView
import com.app.signal.control_kit.recycler_view.adapter.StatefulAdapter
import com.app.signal.control_kit.recycler_view.adapter.ViewHolder
import com.app.signal.discover.R
import com.app.signal.discover.root.model.DiscoverAction
import com.app.signal.discover.root.model.SearchItem


internal class SearchesAdapter : StatefulAdapter<SearchItem>(SearchItem.DIFF) {
    init {
        bind(
            R.layout.discover_rv_search_item, ::SearchHolder
        )
    }

    override fun getItemViewType(model: SearchItem) = R.layout.discover_rv_search_item
}



private class SearchHolder(itemView: View) : ViewHolder<SearchItem>(itemView) {
    private val txtTitle = itemView.findViewById<TextView>(R.id.txt_title)
    override fun bind(item: SearchItem) {
        txtTitle.text = item.text

        itemView.setOnClickListener {
            item.actionFlow.tryEmit(DiscoverAction.Search(item.text))
        }
    }
}