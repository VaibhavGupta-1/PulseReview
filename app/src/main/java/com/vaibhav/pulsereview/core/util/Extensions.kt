package com.vaibhav.pulsereview.core.util

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.update(transform: (T) -> T) {
    value = transform(value)
}
