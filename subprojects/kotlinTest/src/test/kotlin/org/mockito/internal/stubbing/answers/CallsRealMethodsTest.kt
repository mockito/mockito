/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.RETURNS_DEFAULTS
import org.mockito.Mockito.mock
import org.mockito.exceptions.base.MockitoException
import org.mockito.internal.MockitoCore

class CallsRealMethodsTest {

    private interface Foo {
        fun noBody(): String
        fun hasBody(): String = "body"
    }

    private val testTarget = CallsRealMethods()

    @Test
    fun `answer() should delegate to RETURNS_DEFAULTS when calling interface function that has no body`() {
        mock(Foo::class.java).noBody()
        val invocation = MockitoCore().lastInvocation
        assertEquals(RETURNS_DEFAULTS.answer(invocation), testTarget.answer(invocation))
    }

    @Test
    fun `answer() should call real method when calling interface function that has body`() {
        mock(Foo::class.java).hasBody()
        assertEquals("body", testTarget.answer(MockitoCore().lastInvocation))
    }

    @Test
    fun `validateFor() should throw MockitoException when calling interface function that has no body`() {
        mock(Foo::class.java).noBody()
        val invocation = MockitoCore().lastInvocation
        assertTrue(
            try {
                testTarget.validateFor(invocation)
                false
            } catch (ignored: MockitoException) {
                true
            })
    }

    @Test
    fun `validateFor() should be ok when calling interface function that has body`() {
        mock(Foo::class.java).hasBody()
        testTarget.validateFor(MockitoCore().lastInvocation)
    }

}
