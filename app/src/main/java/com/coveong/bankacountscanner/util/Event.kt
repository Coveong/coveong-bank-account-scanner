package com.coveong.bankacountscanner.util

open class Event<out T>(private val extra: T) {
    private var handled: Boolean = false

    fun getExtraIfNotHandled(): T? {
        return if (handled)
            null
        else {
            handled = true
            extra
        }
    }
}
