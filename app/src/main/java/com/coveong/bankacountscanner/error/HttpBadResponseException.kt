package com.coveong.bankacountscanner.error

open class HttpBadResponseException : BusinessException {
    constructor(message: String?) : super(message, ErrorCode.HTTP_BAD_RESPONSE_ERROR) {}
}
