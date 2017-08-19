package org.mockito.kotlin

import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.internal.stubbing.answers.ThrowsException
import org.mockito.invocation.InvocationOnMock

class ThrowsExceptionTest {

    @Test(expected = MyException::class)
    fun `thenThrow should stub the method with any checked exception`() {
        val mock = mock(MyInterface::class.java)
        `when`(mock.doIt()).thenThrow(MyException())
        mock.doIt()
    }

    @Test(expected = MyException::class)
    fun `doThrow should stub the method with any checked exception`() {
        val spied = spy(MyClass::class.java)
        doThrow(MyException()).`when`(spied).doIt()
        spied.doIt()
    }

    @Test
    fun `ThrowsException#validateFor should not check the method signature if the mocked class is of Kotlin`() {
        val invocation = mock(InvocationOnMock::class.java)
        `when`(invocation.mock).thenReturn(mock(MyInterface::class.java))
        `when`(invocation.method).thenThrow(IllegalStateException("Should not be called!"))
        ThrowsException(MyException()).validateFor(invocation)
    }

    interface MyInterface {
        fun doIt(): String
    }

    open class MyClass {
        open fun doIt() = Unit
    }

    class MyException : Exception()

}
