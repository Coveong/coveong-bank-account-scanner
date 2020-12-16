package com.coveong.bankacountscanner.util

import com.coveong.bankacountscanner.models.AccountInfo

object CoveongAccountParser {

    private val allBanks = arrayOf(
        "국민", "카카오", "신한", "농협", "기업", "우리", "하나", "새마을",
        "수협", "우체국", "제일", "부산", "경남", "광주", "대구", "저축",
        "씨티", "신협", "전북", "제주", "산업", "케이", "도이치"
    )

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

    private fun changeNumberWhenSpecialCase(ch: String): String {
        val specialCase0 = arrayOf("0", "o", "O")
        val specialCase1 = arrayOf("1", "|", "l", "/", "(", ")", "I")
        val specialCase01 = arrayOf("01", "이")
        val specialCase4 = arrayOf("4", "+")
        val specialCase5 = arrayOf("5", "f")
        val specialCase7 = arrayOf("7", "n")

        val specialCases = arrayOf(specialCase0, specialCase1, specialCase01, specialCase4, specialCase5, specialCase7)

        for (case in specialCases) {
            for (i in 1 until case.size) {
                if (ch == case[i]) {
                    return case[0]
                }
            }
        }
        
        return ch
    }

    private fun getBankNameByAccount(account: String): String {
        for (bank in allBanks) {
            if (account.contains(bank)) {
                return bank
            }
        }

        return getSimilarBankNameByAccount(account)
    }

    private fun getSimilarBankNameByAccount(account: String): String {
        for (ch in extractKorean(account)) {
            for (bank in allBanks) {
                if (bank.contains(ch)) {
                    return bank
                }
            }
        }

        return ""
    }


    private fun extractKorean(account: String): String {
        val koreanRegex = "[ㄱ-ㅎ가-힣]+".toRegex()
        val result = koreanRegex.find(account)

        return result?.value ?: ""
    }
}
