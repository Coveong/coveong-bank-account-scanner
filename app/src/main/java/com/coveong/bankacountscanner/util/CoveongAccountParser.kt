package com.coveong.bankacountscanner.util

import com.coveong.bankacountscanner.models.AccountInfo
import java.lang.Character.isDigit

object CoveongAccountParser {

    // FIXME: 로직 변경 필요!
    fun parseAccount(account: String): AccountInfo {
        val bankName = getBankNameByAccount(account)
        val accountNumber = getAccountNumberByAccount(account)
        return AccountInfo(bankName, accountNumber)
    }

    private fun getAccountNumberByAccount(account: String): String {
        val result = account.map {
            changeNumberWhenSpecialCase(it.toString())
        }.filter {
            it.matches("-?\\d+(\\.\\d+)?".toRegex())
        }

        return result.joinToString("")
    }

    private fun changeNumberWhenSpecialCase(c: String): String {
        val specialCase0 = arrayOf("0", "o", "O")
        val specialCase1 = arrayOf("1", "|", "l", "/")
        val specialCase01 = arrayOf("01", "이")
        val specialCase7 = arrayOf("7", "n")

        val specialCases = arrayOf(specialCase0, specialCase1, specialCase01, specialCase7)

        for (case in specialCases) {
            for (i in 1 until case.size) {
                if (c == case[i]) {
                    return case[0]
                }
            }
        }
        
        return c
    }

    private fun getBankNameByAccount(account: String): String {
        val allBanks = arrayOf(
            "국민", "카카오", "기업", "농협", "신한", "산업", "우리", "씨티",
            "하나", "제일", "경남", "광주", "대구", "도이치", "부산", "저축",
            "새마을", "수협", "수협", "신협", "우체국", "전북", "제주", "케이"
        )
        for (bank in allBanks) {
            if (account.contains(bank)) {
                return bank
            }
        }
        return ""
    }
}
