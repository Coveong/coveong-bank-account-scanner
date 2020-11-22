package com.coveong.bankacountscanner.error

class ExternalAppNotInstalledException(message: String?) : BusinessException(message, ErrorCode.EXTERNAL_APP_NOT_INSTALLED_ERROR)
