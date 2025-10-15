/**
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.kotlin

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

@JvmInline
value class IC(val i: Int)
@JvmInline
value class ICN(val i: Int?)
@JvmInline
value class SC(val s: String)
@JvmInline
value class SCN(val s: String?)

class InlineClassTest {
    interface I {
        fun acceptsIC(ic: IC): Int
        fun returnsIC(i: Int): IC

        fun acceptsIC_N(ic: IC?): Int
        fun returnsIC_N(i: Int): IC?

        fun acceptsICN(ic: ICN): Int
        fun returnsICN(i: Int): ICN

        fun acceptsICN_N(ic: ICN?): Int
        fun returnsICN_N(i: Int): ICN?

        fun acceptsSC(ic: SC): Int
        fun returnsSC(i: Int): SC

        fun acceptsSC_N(ic: SC?): Int
        fun returnsSC_N(i: Int): SC?

        fun acceptsSCN(ic: SCN): Int
        fun returnsSCN(i: Int): SCN

        fun acceptsSCN_N(ic: SCN?): Int
        fun returnsSCN_N(i: Int): SCN?
    }

    class Caller(val i: I) {
        fun callAcceptsIC(ic: IC): Int = i.acceptsIC(ic)
        fun callReturnsIC(int: Int): IC = i.returnsIC(int)

        fun callAcceptsIC_N(ic: IC?): Int = i.acceptsIC_N(ic)
        fun callReturnsIC_N(int: Int): IC? = i.returnsIC_N(int)

        fun callAcceptsICN(ic: ICN): Int = i.acceptsICN(ic)
        fun callReturnsICN(int: Int): ICN = i.returnsICN(int)

        fun callAcceptsICN_N(ic: ICN?): Int = i.acceptsICN_N(ic)
        fun callReturnsICN_N(int: Int): ICN? = i.returnsICN_N(int)

        fun callAcceptsSC(ic: SC): Int = i.acceptsSC(ic)
        fun callReturnsSC(int: Int): SC = i.returnsSC(int)

        fun callAcceptsSC_N(ic: SC?): Int = i.acceptsSC_N(ic)
        fun callReturnsSC_N(int: Int): SC? = i.returnsSC_N(int)

        fun callAcceptsSCN(ic: SCN): Int = i.acceptsSCN(ic)
        fun callReturnsSCN(int: Int): SCN = i.returnsSCN(int)

        fun callAcceptsSCN_N(ic: SCN?): Int = i.acceptsSCN_N(ic)
        fun callReturnsSCN_N(int: Int): SCN? = i.returnsSCN_N(int)
    }

    @Test
    fun testAcceptsIC() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.acceptsIC(IC(1))).thenReturn(1)
        `when`(mock.acceptsIC(IC(2))).thenReturn(2)

        assertEquals(1, caller.callAcceptsIC(IC(1)))
        assertEquals(2, caller.callAcceptsIC(IC(2)))

        verify(mock).acceptsIC(IC(1))
        verify(mock).acceptsIC(IC(2))
    }

    @Test
    fun testReturnsIC() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.returnsIC(1)).thenReturn(IC(1))
        `when`(mock.returnsIC(2)).thenReturn(IC(2))

        assertEquals(IC(1), caller.callReturnsIC(1))
        assertEquals(IC(2), caller.callReturnsIC(2))

        verify(mock).returnsIC(1)
        verify(mock).returnsIC(2)
    }

    @Test
    fun testAcceptsIC_N() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.acceptsIC_N(IC(1))).thenReturn(1)
        `when`(mock.acceptsIC_N(null)).thenReturn(0)

        assertEquals(1, caller.callAcceptsIC_N(IC(1)))
        assertEquals(0, caller.callAcceptsIC_N(null))

        verify(mock).acceptsIC_N(IC(1))
        verify(mock).acceptsIC_N(null)
    }

    @Test
    fun testReturnsIC_N() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.returnsIC_N(1)).thenReturn(IC(1))
        `when`(mock.returnsIC_N(0)).thenReturn(null)

        assertEquals(IC(1), caller.callReturnsIC_N(1))
        assertEquals(null, caller.callReturnsIC_N(0))

        verify(mock).returnsIC_N(1)
        verify(mock).returnsIC_N(0)
    }

    @Test
    fun testAcceptsICN() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.acceptsICN(ICN(1))).thenReturn(1)
        `when`(mock.acceptsICN(ICN(null))).thenReturn(0)

        assertEquals(1, caller.callAcceptsICN(ICN(1)))
        assertEquals(0, caller.callAcceptsICN(ICN(null)))

        verify(mock).acceptsICN(ICN(1))
        verify(mock).acceptsICN(ICN(null))
    }

    @Test
    fun testReturnsICN() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.returnsICN(1)).thenReturn(ICN(1))
        `when`(mock.returnsICN(0)).thenReturn(ICN(null))

        assertEquals(ICN(1), caller.callReturnsICN(1))
        assertEquals(ICN(null), caller.callReturnsICN(0))

        verify(mock).returnsICN(1)
        verify(mock).returnsICN(0)
    }

    @Test
    fun testAcceptsICN_N() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.acceptsICN_N(ICN(1))).thenReturn(1)
        `when`(mock.acceptsICN_N(null)).thenReturn(0)
        `when`(mock.acceptsICN_N(ICN(null))).thenReturn(-1)

        assertEquals(1, caller.callAcceptsICN_N(ICN(1)))
        assertEquals(0, caller.callAcceptsICN_N(null))
        assertEquals(-1, caller.callAcceptsICN_N(ICN(null)))

        verify(mock).acceptsICN_N(ICN(1))
        verify(mock).acceptsICN_N(null)
        verify(mock).acceptsICN_N(ICN(null))
    }

    @Test
    fun testReturnsICN_N() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.returnsICN_N(1)).thenReturn(ICN(1))
        `when`(mock.returnsICN_N(0)).thenReturn(null)
        `when`(mock.returnsICN_N(-1)).thenReturn(ICN(null))

        assertEquals(ICN(1), caller.callReturnsICN_N(1))
        assertEquals(null, caller.callReturnsICN_N(0))
        assertEquals(ICN(null), caller.callReturnsICN_N(-1))

        verify(mock).returnsICN_N(1)
        verify(mock).returnsICN_N(0)
        verify(mock).returnsICN_N(-1)
    }

    @Test
    fun testAcceptsSC() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.acceptsSC(SC("1"))).thenReturn(1)
        `when`(mock.acceptsSC(SC("2"))).thenReturn(2)

        assertEquals(1, caller.callAcceptsSC(SC("1")))
        assertEquals(2, caller.callAcceptsSC(SC("2")))

        verify(mock).acceptsSC(SC("1"))
        verify(mock).acceptsSC(SC("2"))
    }

    @Test
    fun testReturnsSC() {
        val mock = mock(I::class.java, withSettings().name("").defaultAnswer(RETURNS_MOCKS))
        val caller = Caller(mock)

        `when`(mock.returnsSC(1)).thenReturn(SC("1"))
        `when`(mock.returnsSC(2)).thenReturn(SC("2"))

        assertEquals(SC("1"), caller.callReturnsSC(1))
        assertEquals(SC("2"), caller.callReturnsSC(2))

        verify(mock).returnsSC(1)
        verify(mock).returnsSC(2)
    }

    @Test
    fun testAcceptsSC_N() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.acceptsSC_N(SC("1"))).thenReturn(1)
        `when`(mock.acceptsSC_N(null)).thenReturn(0)

        assertEquals(1, caller.callAcceptsSC_N(SC("1")))
        assertEquals(0, caller.callAcceptsSC_N(null))

        verify(mock).acceptsSC_N(SC("1"))
        verify(mock).acceptsSC_N(null)
    }

    @Test
    fun testReturnsSC_N() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.returnsSC_N(1)).thenReturn(SC("1"))
        `when`(mock.returnsSC_N(0)).thenReturn(null)

        assertEquals(SC("1"), caller.callReturnsSC_N(1))
        assertEquals(null, caller.callReturnsSC_N(0))

        verify(mock).returnsSC_N(1)
        verify(mock).returnsSC_N(0)
    }

    @Test
    fun testAcceptsSCN() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.acceptsSCN(SCN("1"))).thenReturn(1)
        `when`(mock.acceptsSCN(SCN(null))).thenReturn(0)

        assertEquals(1, caller.callAcceptsSCN(SCN("1")))
        assertEquals(0, caller.callAcceptsSCN(SCN(null)))

        verify(mock).acceptsSCN(SCN("1"))
        verify(mock).acceptsSCN(SCN(null))
    }

    @Test
    fun testReturnsSCN() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.returnsSCN(1)).thenReturn(SCN("1"))
        `when`(mock.returnsSCN(0)).thenReturn(SCN(null))

        assertEquals(SCN("1"), caller.callReturnsSCN(1))
        assertEquals(SCN(null), caller.callReturnsSCN(0))

        verify(mock).returnsSCN(1)
        verify(mock).returnsSCN(0)
    }

    @Test
    fun testAcceptsSCN_N() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.acceptsSCN_N(SCN("1"))).thenReturn(1)
        `when`(mock.acceptsSCN_N(null)).thenReturn(0)
        `when`(mock.acceptsSCN_N(SCN(null))).thenReturn(-1)

        assertEquals(1, caller.callAcceptsSCN_N(SCN("1")))
        assertEquals(0, caller.callAcceptsSCN_N(null))
        assertEquals(-1, caller.callAcceptsSCN_N(SCN(null)))

        verify(mock).acceptsSCN_N(SCN("1"))
        verify(mock).acceptsSCN_N(null)
        verify(mock).acceptsSCN_N(SCN(null))
    }

    @Test
    fun testReturnsSCN_N() {
        val mock = mock(I::class.java)
        val caller = Caller(mock)

        `when`(mock.returnsSCN_N(1)).thenReturn(SCN("1"))
        `when`(mock.returnsSCN_N(0)).thenReturn(null)
        `when`(mock.returnsSCN_N(-1)).thenReturn(SCN(null))

        assertEquals(SCN("1"), caller.callReturnsSCN_N(1))
        assertEquals(null, caller.callReturnsSCN_N(0))
        assertEquals(SCN(null), caller.callReturnsSCN_N(-1))

        verify(mock).returnsSCN_N(1)
        verify(mock).returnsSCN_N(0)
        verify(mock).returnsSCN_N(-1)
    }

    @Suppress("RESULT_CLASS_IN_RETURN_TYPE")
    interface WithResult {
        fun returnsResult(): Result<String>
    }

    @Suppress("RESULT_CLASS_IN_RETURN_TYPE")
    class WithResultCaller(val i: WithResult) {
        fun callReturnsResult(): Result<String> = i.returnsResult()
    }

    @Test
    fun testResult() {
        val mock = mock(WithResult::class.java)
        val caller = WithResultCaller(mock)

        `when`(mock.returnsResult()).thenReturn(Result.success("OK"))

        assertEquals(Result.success("OK"), caller.callReturnsResult())

        verify(mock).returnsResult()
    }

    @Test
    @SuppressWarnings("DoNotMock", "DoNotMockAutoValue")
    fun automaticallyDetectsClassToMock() {
        val mock: WithResult = mock()

        `when`(mock.returnsResult()).thenReturn(Result.success("OK"))

        assertEquals("OK", mock.returnsResult().getOrNull())
    }
}
