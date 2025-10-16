package com.roque.domain.common

sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String, val exception: Throwable? = null) : UIState<Nothing>()
}

inline fun <T> UIState<T>.onSuccess(action: (value: T) -> Unit): UIState<T> {
    if (this is UIState.Success) action(data)
    return this
}

inline fun <T> UIState<T>.onError(action: (message: String, exception: Throwable?) -> Unit): UIState<T> {
    if (this is UIState.Error) action(message, exception)
    return this
}

inline fun <T> UIState<T>.onLoading(action: () -> Unit): UIState<T> {
    if (this is UIState.Loading) action()
    return this
}