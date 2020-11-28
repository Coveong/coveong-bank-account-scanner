package com.coveong.bankacountscanner.ui

import androidx.appcompat.app.AppCompatActivity
import com.coveong.bankacountscanner.error.BusinessException
import com.coveong.bankacountscanner.error.ErrorHandler
import com.coveong.bankacountscanner.error.showErrorToast

open class BaseActivity : AppCompatActivity(), ErrorHandler {
    override fun onUnknownError(e: Throwable) {
        runOnUiThread {
            showErrorToast(e)
        }
    }

    override fun onBusinessError(e: BusinessException) {
        runOnUiThread {
            showErrorToast(e)
        }
    }
}
