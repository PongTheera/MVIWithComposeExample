package com.example.mviwithcomposeexample

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

        val testList = listOf<ProductTest>(ProductTest(0, "name0"), ProductTest(1, "name1"), ProductTest(2, "name2"))

        testList.maxOf { it.id }.also {
            println("max number = $it")
        }
    }

    data class ProductTest(
        val id: Int,
        val name: String
    )
}