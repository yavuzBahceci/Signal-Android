package com.app.signal.control_kit.ex

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

inline fun<reified T: RecyclerView.Adapter<*>> ConcatAdapter.getAdapter(): T {
    return adapters.first { it is T } as T
}

inline fun<reified T: RecyclerView.Adapter<*>> ConcatAdapter.getAdapterOrNull(): T? {
    return adapters.firstOrNull { it is T } as? T
}

inline fun<reified T: RecyclerView.Adapter<*>> ConcatAdapter.getAdapter(filter: (T) -> Boolean): T {
    return adapters.first {
        it is T && filter(it)
    } as T
}

inline fun<reified T: RecyclerView.Adapter<*>> ConcatAdapter.getAdapterIdx(): Int {
    return adapters.indexOfFirst {
        it is T
    }
}