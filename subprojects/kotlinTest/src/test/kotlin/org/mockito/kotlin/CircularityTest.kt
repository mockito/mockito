package org.mockito.kotlin

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MyClass

class MyTest {

    private lateinit var myClassMock: MyClass

    @Before
    fun `before each`() {
        myClassMock = Mockito.mock(MyClass::class.java)
    }

    @Test
    fun `some test`() { }
}
