package com.coveong.bankacountscanner.models

data class AccountInfo constructor(
    var bankName: String,
    var account: String
) {
    override fun toString(): String = "$bankName $account"
}
