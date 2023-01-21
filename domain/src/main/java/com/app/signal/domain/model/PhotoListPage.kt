package com.app.signal.domain.model

data class PhotoListPage<out T>(
    val items: List<T>,
    val nextPage: Long?
) {
    companion object {
        fun <T> empty(): PhotoListPage<T> {
            return PhotoListPage(emptyList(), null)
        }
    }
}