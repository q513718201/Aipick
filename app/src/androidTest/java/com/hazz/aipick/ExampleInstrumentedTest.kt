package com.hazz.aipick


import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val temp = mutableListOf<String>()
        temp.add("2020-01-02")
        temp.add("2020-02-03")
        temp.add("2020-01-03")
        temp.sort()
        println(temp)
    }
}
