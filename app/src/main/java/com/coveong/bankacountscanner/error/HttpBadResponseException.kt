package com.coveong.bankacountscanner.error

open class HttpBadResponseException(message: String? = null) :
    BusinessException(message, ErrorCode.HTTP_BAD_RESPONSE_ERROR)
