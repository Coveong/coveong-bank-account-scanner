package com.coveong.bankacountscanner.error

class CameraException(message: String? = null) :
    BusinessException(message, ErrorCode.CAMERA_ERROR)
