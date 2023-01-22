package com.app.saved.root.model

sealed class SavedAction {
    data class Select(val photo: SavedItem.Photo): SavedAction()
    data class Delete(val photo: SavedItem.Photo): SavedAction()
}