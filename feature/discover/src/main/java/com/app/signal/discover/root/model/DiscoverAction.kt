package com.app.signal.discover.root.model

sealed class DiscoverAction {
    data class Select(val photo: DiscoverItem.Photo): DiscoverAction()
    data class Save(val photo: DiscoverItem.Photo): DiscoverAction()
    data class Search(val searchText: String): DiscoverAction()
}