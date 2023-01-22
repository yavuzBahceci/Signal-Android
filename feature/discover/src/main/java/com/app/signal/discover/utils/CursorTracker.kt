package com.app.signal.discover.utils

import com.app.signal.domain.model.PhotoListPage
import com.app.signal.domain.model.State
import kotlinx.coroutines.flow.*

sealed class CursorTracker {
    data class LoadMore(val nextPage: Long? = null) : CursorTracker()
    object EndOfList : CursorTracker()
}

fun <T> cursorFlow(
    cursorTracker: CursorTracker = CursorTracker.LoadMore(),
    trigger: Flow<Unit>,
    request: (Long?) -> Flow<State<PhotoListPage<T>>>
): Flow<State<List<T>>> {
    return channelFlow {
        var tracker = cursorTracker
        val buffer = mutableListOf<T>()

        trigger
            .onStart {
                emit(Unit)
            }
            .collectLatest {
                val key: Long?


                when (val tmp = tracker) {
                    is CursorTracker.LoadMore -> {
                        key = tmp.nextPage
                        println("!!!!!!!! cursorFlow Collect Latest Key: ${key.toString()}")
                    }
                    else -> return@collectLatest
                }

                if (buffer.isEmpty()) {
                    send(State.Loading())
                }

                val result = request(key)
                    .filterNot { it.isLoading }
                    .map { it.data }
                    .first()

                result?.let {
                    println("!!!!!!! New Result")
                    buffer.addAll(result.items)

                    val next = result.nextPage

                    tracker = if (next != null) {
                        CursorTracker.LoadMore(next)
                    } else {
                        CursorTracker.EndOfList
                    }
                }

                send(State.Success(buffer))
            }
    }
}