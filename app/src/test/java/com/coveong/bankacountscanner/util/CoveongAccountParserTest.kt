package com.coveong.bankacountscanner.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CoveongAccountParserTest {

    @Test
    fun 숫자랑_비슷한_문자는_숫자로_치환한다_0() {
        assertThat(CoveongAccountParser.parseAccount("oO").account).isEqualTo("00")
    }

    @Test
    fun 숫자랑_비슷한_문자는_숫자로_치환한다_1() {
        assertThat(CoveongAccountParser.parseAccount("|l/()I").account).isEqualTo("111111")
    }

    @Test
    fun 숫자랑_비슷한_문자는_숫자로_치환한다_01() {
        assertThat(CoveongAccountParser.parseAccount("이이").account).isEqualTo("0101")
    }

    @Test
    fun 숫자랑_비슷한_문자는_숫자로_치환한다_4() {
        assertThat(CoveongAccountParser.parseAccount("+").account).isEqualTo("4")
    }

    @Test
    fun 숫자랑_비슷한_문자는_숫자로_치환한다_5() {
        assertThat(CoveongAccountParser.parseAccount("f").account).isEqualTo("5")
    }


    @Test
    fun 숫자랑_비슷한_문자는_숫자로_치환한다_7() {
        assertThat(CoveongAccountParser.parseAccount("n").account).isEqualTo("7")
    }

    @Test
    fun 해당하는_은행이름만_추출한다() {
        assertThat(CoveongAccountParser.parseAccount("기업홍길동0001").bankName).isEqualTo("기업")
        assertThat(CoveongAccountParser.parseAccount("카카오짜장").bankName).isEqualTo("카카오")
        assertThat(CoveongAccountParser.parseAccount("하하호호새마을").bankName).isEqualTo("새마을")
    }

    @Test
    fun 숫자는_계좌로_추출한다() {
        assertThat(CoveongAccountParser.parseAccount("기업홍길동0001").account).isEqualTo("0001")
        assertThat(CoveongAccountParser.parseAccount("가 193949").account).isEqualTo("193949")
        assertThat(CoveongAccountParser.parseAccount("새마을 1111").account).isEqualTo("1111")
    }

    @Test
    fun 은행이름중_한글자만_인식되어도_전체로_치환한다() {
        assertThat(CoveongAccountParser.parseAccount("농1").bankName).isEqualTo("농협")
        assertThat(CoveongAccountParser.parseAccount("1새22홍길동").bankName).isEqualTo("새마을")
        assertThat(CoveongAccountParser.parseAccount("카오").bankName).isEqualTo("카카오")
    }
}
