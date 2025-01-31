package org.chiuxah.blog.logic.uitls

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow


fun <T> MutableStateFlow<T?>.reset() {
    value = null
}

suspend fun <T> MutableSharedFlow<T>.reset() {
    emit(null as T) // SharedFlow 需要使用 emit
}

