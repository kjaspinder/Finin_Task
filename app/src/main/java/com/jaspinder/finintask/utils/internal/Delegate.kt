package com.jaspinder.finintask.utils.internal

import kotlinx.coroutines.*

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.DEFAULT) {
            block.invoke(this)
        }
    }
}