package com.coveong.bankacountscanner.error

enum class ErrorCode(val status: Int, val message: String) {
    // Camera
    CAMERA_ERROR(500, "Camera related error."),
    HTTP_BAD_RESPONSE_ERROR(500, "The http response result is abnormal."),
    NOT_INSTALLED_APP_ERROR(500, "The app is not installed."),
}
