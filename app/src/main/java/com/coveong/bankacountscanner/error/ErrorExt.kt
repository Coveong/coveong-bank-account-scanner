package com.coveong.bankacountscanner.error

import android.app.Activity
import android.content.res.Resources
import android.widget.Toast
import com.coveong.bankacountscanner.BuildConfig
import com.coveong.bankacountscanner.R

val BusinessException.messageResId: Int?
    get() = when (this) {
        is CameraException ->
            R.string.error_camera_exception
        is ExternalAppNotInstalledException ->
            R.string.error_external_app_not_installed_exception
        is HttpBadResponseException ->
            R.string.error_http_bad_response_exception
        else -> null
    }

fun Throwable.getUnknownErrorMessage(resources: Resources): String {
    return if (BuildConfig.DEBUG) {
        resources.getString(R.string.error_unknown_debug, toString())
    } else {
        resources.getString(R.string.error_unknown_release)
    }
}

fun BusinessException.getBusinessErrorMessage(resources: Resources): String {
    val messageResId = messageResId
    return if (messageResId == null) {
        getUnknownErrorMessage(resources)
    } else {
        resources.getString(messageResId)
    }
}

fun Activity.showErrorToast(e: Throwable) {
    val message = when (e) {
        is BusinessException -> e.getBusinessErrorMessage(resources)
        else -> e.getUnknownErrorMessage(resources)
    }
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

