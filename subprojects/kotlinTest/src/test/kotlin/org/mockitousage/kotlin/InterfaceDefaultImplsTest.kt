/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.kotlin

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class InterfaceDefaultImplsTest {

    private interface Foo {
        fun f1(): String = "Foo.f1()"
        fun f2(): String = "Foo.f2()"
        fun f3(arg: String): String = "Foo.f3(\"$arg\")"
    }

    private interface Bar : Foo {
        override fun f2(): String = "Bar.f2()"
        override fun f3(arg: String): String = "Bar.f3(\"$arg\")"
    }

    @Test
    fun `should call default implementation of interface function`() {
        val mock = mock(Foo::class.java)
        given(mock.f1()).willCallRealMethod()
        assertEquals("Foo.f1()", mock.f1())
    }

    @Test
    fun `should call default implementation of interface function with String parameter`() {
        val mock = mock(Foo::class.java)
        given(mock.f3("test")).willCallRealMethod()
        assertEquals("Foo.f3(\"test\")", mock.f3("test"))
    }

    @Test
    fun `should call default implementation of super interface function`() {
        val mock = mock(Bar::class.java)
        given(mock.f1()).willCallRealMethod()
        assertEquals("Foo.f1()", mock.f1())
    }

    @Test
    fun `should call default implementation of overridden interface function`() {
        val mock = mock(Bar::class.java)
        given(mock.f2()).willCallRealMethod()
        assertEquals("Bar.f2()", mock.f2())
    }

    @Test
    fun `should call default implementation of overridden interface function with String parameter`() {
        val mock = mock(Bar::class.java)
        given(mock.f3("test")).willCallRealMethod()
        assertEquals("Bar.f3(\"test\")", mock.f3("test"))
    }

}
