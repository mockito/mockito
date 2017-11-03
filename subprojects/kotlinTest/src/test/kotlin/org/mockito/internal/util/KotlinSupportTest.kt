/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.internal.invocation.RealMethod

class KotlinSupportTest {

    private interface Foo {
        fun noBody(): String
        fun hasBody(): String = "body"
        fun thrownInBody() {
            throw MyException()
        }
    }

    private class MyException : Exception()

    @Test
    fun `hasDefaultImplementation() should return false if the given interface method has no body`() {
        assertFalse(KotlinSupport.hasDefaultImplementation(Foo::class.java.getMethod("noBody")))
    }

    @Test
    fun `hasDefaultImplementation() should return true if the given interface method has body`() {
        assertTrue(KotlinSupport.hasDefaultImplementation(Foo::class.java.getMethod("hasBody")))
    }

    @Test
    fun `getDefaultImplementation() should return notFound if the given interface method has no body`() {
        val mock = mock(Foo::class.java)
        val method = Foo::class.java.getMethod("noBody")
        val notFound = RealMethod.IsIllegal.INSTANCE
        assertSame(notFound, KotlinSupport.getDefaultImplementation(mock, method, emptyArray(), notFound))
    }

    @Test
    fun `getDefaultImplementation() should return real method representing default implementation`() {
        val mock = mock(Foo::class.java)
        val method = Foo::class.java.getMethod("hasBody")
        val notFound = RealMethod.IsIllegal.INSTANCE
        val realMethod = KotlinSupport.getDefaultImplementation(mock, method, emptyArray(), notFound)
        assertNotSame(notFound, realMethod)
        assertTrue(realMethod.isInvokable)
        assertEquals("body", realMethod.invoke())
    }

    @Test
    fun `Filtered exception should be thrown when calling default implementation that raises an exception`() {
        val mock = mock(Foo::class.java)
        val method = Foo::class.java.getMethod("thrownInBody")
        val notFound = RealMethod.IsIllegal.INSTANCE
        val realMethod = KotlinSupport.getDefaultImplementation(mock, method, emptyArray(), notFound)
        assertNotSame(notFound, realMethod)
        assertTrue(realMethod.isInvokable)
        assertTrue(try {
            realMethod.invoke()
            false
        } catch (e: MyException) {
            e.stackTrace.none { it.className.startsWith("org.mockito.") }
        })
    }

}
