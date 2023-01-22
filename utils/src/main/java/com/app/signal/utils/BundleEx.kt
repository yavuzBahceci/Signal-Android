package com.app.signal.utils

import android.os.Build
import android.os.Bundle

inline fun<reified T: Enum<T>> Bundle.getEnum(key: String): T? {
    val name = getString(key) ?: return null
    return enumValues<T>().firstOrNull { it.name == name }
}

inline fun <reified T> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T> Bundle.parcelableArrayList(key: String): List<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}