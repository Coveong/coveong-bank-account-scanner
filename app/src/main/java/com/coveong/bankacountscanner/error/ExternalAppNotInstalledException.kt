package com.coveong.bankacountscanner.error

class ExternalAppNotInstalledException(message: String? = null) :
    BusinessException(message, ErrorCode.EXTERNAL_APP_NOT_INSTALLED_ERROR)
