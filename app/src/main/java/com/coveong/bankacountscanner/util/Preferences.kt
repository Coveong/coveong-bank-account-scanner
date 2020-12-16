package com.coveong.bankacountscanner.util

import android.content.Context
import androidx.preference.PreferenceManager

class Preferences(context: Context) {

    private var preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private var _alertBeforeStartDialogShowed: Boolean = false
    var alertBeforeStartDialogShowed: Boolean
        get() {
            _alertBeforeStartDialogShowed = preferences.getBoolean(
                ALERT_BEFORE_START_DIALOG_SHOWED_FLAG, false
            )
            return _alertBeforeStartDialogShowed
        }
        set(flag) {
            preferences.edit().putBoolean(ALERT_BEFORE_START_DIALOG_SHOWED_FLAG, flag).apply()
            _alertBeforeStartDialogShowed = flag
        }

    companion object {
        private const val ALERT_BEFORE_START_DIALOG_SHOWED_FLAG = "alertBeforeStartDialogShowed"
    }
}
