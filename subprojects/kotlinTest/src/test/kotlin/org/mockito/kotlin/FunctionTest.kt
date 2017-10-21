package org.mockito.kotlin

import org.junit.Test
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

class FunctionTest {

    @Test
    fun test() {
        val value = spy({})
        value.invoke()

        verify(value).invoke()
    }

    @Test
    fun testAgain() {
        val value = spy({})
        value.invoke()

        verify(value).invoke()
    }
}
