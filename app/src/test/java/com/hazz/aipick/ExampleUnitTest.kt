package com.hazz.aipick

import com.hazz.aipick.utils.RsaUtils.jiami
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        var st = "-111"
        println(st[0].isDigit())
        val phone = jiami("18775940437")
        val pwd = jiami("mo123456")
        println(phone)
        println(pwd)
    }


}
