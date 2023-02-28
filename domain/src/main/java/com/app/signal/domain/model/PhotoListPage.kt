package com.app.signal.domain.model

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

data class PhotoListPage<out T>(
    val items: List<T>,
    val pageCount: Long,
    val currentPage: Long
) {
    val nextPage: Long? by NextPageDelegate()
}

class NextPageDelegate<T> :
    ReadOnlyProperty<PhotoListPage<T>, Long?> {

    override fun getValue(thisRef: PhotoListPage<T>, property: KProperty<*>): Long? {
        return if (thisRef.currentPage < thisRef.pageCount) {
            thisRef.currentPage + 1
        } else {
            null
        }
    }

}
