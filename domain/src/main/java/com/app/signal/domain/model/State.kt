package com.app.signal.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

typealias VoidState = State<Unit>
typealias BooleanState = State<Boolean>
typealias OptionalBooleanState = State<Boolean?>

sealed class State<out T> {
    data class Success<T>(val value: T) : State<T>()
    data class Loading<T>(val value: T? = null) : State<T>()
    data class Error<T>(val cause: Throwable, val value: T? = null) : State<T>()
    class Empty<T> : State<T>()

    val data: T?
        get() = when (this) {
            is Success -> value
            is Loading -> value
            is Error -> value
            else -> null
        }

    val error: Throwable?
        get() = when (this) {
            is Error -> cause
            else -> null
        }

    val dropData: State<Unit>
        get() = when (this) {
            is Success -> Success(Unit)
            is Loading -> Loading(Unit)
            is Error -> Error(cause, Unit)
            is Empty -> Empty()
        }

    val isSuccess: Boolean get() = this is Success
    val isLoading: Boolean get() = this is Loading
    val isError: Boolean get() = this is Error

    inline fun <reified R> map(crossinline transform: (value: T) -> R): State<R> {
        return when (this) {
            is Success -> Success(transform(value))

            is Loading -> Loading(data?.let(transform))

            is Error -> Error(cause, data?.let(transform))

            is Empty -> Empty()
        }
    }

    inline fun <reified R> mapOnSuccess(crossinline transform: (value: T) -> R): State<R> {
        return when (this) {
            is Success -> Success(transform(value))
            is Loading -> Loading(null)
            is Error -> Error(cause, null)
            is Empty -> Empty()
        }
    }

    suspend inline fun <reified R> mapAsync(crossinline transform: suspend (value: T?) -> R): State<R> {
        val transformed = transform(data)

        return when (this) {
            is Success -> Success(transformed)
            is Loading -> Loading(transformed)
            is Error -> Error(cause, transformed)
            is Empty -> Empty()
        }
    }
}

inline fun <T, reified R> State<List<T>>.mapListItem(crossinline transform: (value: T) -> R): State<List<R>> {
    return map { it.map(transform) }
}

inline fun <T, R : Comparable<R>> State<List<T>>.sortedBy(crossinline selector: (T) -> R?): State<List<T>> {
    return map { it.sortedWith(compareBy(selector)) }
}

fun <T> Flow<State<T>>.dropStateData(): Flow<VoidState> {
    return map { it.map { } }
}

inline fun <T, reified R> Flow<State<T>>.mapState(crossinline transform: (value: T?) -> R): Flow<State<R>> {
    return map { it.map(transform) }
}

inline fun <T, reified R> Flow<State<T>>.mapStateOnSuccess(crossinline transform: (value: T) -> R): Flow<State<R>> {
    return map { it.mapOnSuccess(transform) }
}

inline fun <T, reified R> Flow<State<T>>.mapStateAsync(crossinline transform: suspend (value: T?) -> R): Flow<State<R>> {
    return map { it.mapAsync(transform) }
}

inline fun <T, reified R> Flow<State<List<T>>>.mapStateListItem(
    crossinline transform: (value: T) -> R
): Flow<State<List<R>>> {
    return map { it.mapListItem(transform) }
}
