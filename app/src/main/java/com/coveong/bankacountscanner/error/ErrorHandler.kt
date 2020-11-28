package com.coveong.bankacountscanner.error


interface ErrorHandler {
    fun onUnknownError(e: Throwable)
    fun onBusinessError(e: BusinessException)
}

fun ErrorHandler.handleError(e: Throwable) {
    when (e) {
        is BusinessException -> onBusinessError(e)
        else -> onUnknownError(e)
    }
}
