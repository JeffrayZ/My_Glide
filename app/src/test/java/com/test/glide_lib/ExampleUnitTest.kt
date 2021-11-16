package com.test.glide_lib

import org.junit.Test

import org.junit.Assert.*
import kotlin.concurrent.thread

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testSyn(){
        val testSyn = SyncTest()

        thread {
            testSyn.obj2()
        }.start()

        thread {
            testSyn.obj3()
        }.start()
    }
}