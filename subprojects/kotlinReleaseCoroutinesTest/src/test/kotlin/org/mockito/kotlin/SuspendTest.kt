/**
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.kotlin

import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Test for mocking and verifying Kotlin suspending functions.
 */
class SuspendTest {
    interface SuspendableInterface {
        suspend fun suspendFunction(x: Int): Int
        suspend fun suspendFunctionWithDefault(x: Int = 1): Int
    }

    open class SuspendableClass : SuspendableInterface {
        suspend override fun suspendFunction(x: Int): Int = error("not implemented")
        suspend override fun suspendFunctionWithDefault(x: Int): Int = error("not implemented")
        suspend open fun suspendClassFunctionWithDefault(x: Int = 1): Int = error("not implemented")
    }

    class FinalSuspendableClass {
        suspend fun fetch(): Int {
            fetchConcrete()
            return 2
        }

        suspend fun fetchConcrete() = 1
    }

    open class CallsSuspendable(private val suspendable: SuspendableInterface) {
        fun callSuspendable(x: Int) = runBlocking {
            suspendable.suspendFunction(x)
        }

        fun suspendFunctionWithDefault() = runBlocking {
            suspendable.suspendFunctionWithDefault()
        }

        fun suspendFunctionWithDefault(x: Int) = runBlocking {
            suspendable.suspendFunctionWithDefault(x)
        }
    }

    class CallsSuspendableClass(private val suspendable: SuspendableClass) : CallsSuspendable(suspendable) {
        fun suspendClassFunctionWithDefault() = runBlocking {
            suspendable.suspendClassFunctionWithDefault()
        }

        fun suspendClassFunctionWithDefault(x: Int) = runBlocking {
            suspendable.suspendClassFunctionWithDefault(x)
        }
    }

    @Test
    fun shouldMockSuspendInterfaceFunction() = runBlocking<Unit> {
        val mockSuspendable: SuspendableInterface = mock(SuspendableInterface::class.java)
        val callsSuspendable = CallsSuspendable(mockSuspendable)

        `when`(mockSuspendable.suspendFunction(1)).thenReturn(2)
        `when`(mockSuspendable.suspendFunction(intThat { it >= 2 })).thenReturn(4)

        assertThat(callsSuspendable.callSuspendable(1), IsEqual(2))
        assertThat(callsSuspendable.callSuspendable(2), IsEqual(4))

        verify(mockSuspendable).suspendFunction(1)
        verify(mockSuspendable).suspendFunction(2)
    }

    @Test
    fun shouldMockSuspendInterfaceFunctionWithDefault() = runBlocking<Unit> {
        val mockSuspendable: SuspendableInterface = mock(SuspendableInterface::class.java)
        val callsSuspendable = CallsSuspendable(mockSuspendable)

        `when`(mockSuspendable.suspendFunctionWithDefault()).thenReturn(2)
        `when`(mockSuspendable.suspendFunctionWithDefault(intThat { it >= 2 })).thenReturn(4)

        assertThat(callsSuspendable.suspendFunctionWithDefault(), IsEqual(2))
        assertThat(callsSuspendable.suspendFunctionWithDefault(2), IsEqual(4))

        verify(mockSuspendable).suspendFunctionWithDefault()
        verify(mockSuspendable).suspendFunctionWithDefault(2)
    }

    @Test
    fun shouldMockSuspendClassFunction() = runBlocking<Unit> {
        val mockSuspendable: SuspendableClass = mock(SuspendableClass::class.java)
        val callsSuspendable = CallsSuspendable(mockSuspendable)

        `when`(mockSuspendable.suspendFunction(1)).thenReturn(2)
        `when`(mockSuspendable.suspendFunction(intThat { it >= 2 })).thenReturn(4)

        assertThat(callsSuspendable.callSuspendable(1), IsEqual(2))
        assertThat(callsSuspendable.callSuspendable(2), IsEqual(4))

        verify(mockSuspendable).suspendFunction(1)
        verify(mockSuspendable).suspendFunction(2)
    }

    @Test
    fun shouldMockSuspendClassFunctionWithDefault() = runBlocking<Unit> {
        val mockSuspendable: SuspendableClass = mock(SuspendableClass::class.java)
        val callsSuspendable = CallsSuspendableClass(mockSuspendable)

        `when`(mockSuspendable.suspendClassFunctionWithDefault()).thenReturn(2)
        `when`(mockSuspendable.suspendClassFunctionWithDefault(intThat { it >= 2 })).thenReturn(4)

        assertThat(callsSuspendable.suspendClassFunctionWithDefault(), IsEqual(2))
        assertThat(callsSuspendable.suspendClassFunctionWithDefault(2), IsEqual(4))

        verify(mockSuspendable).suspendClassFunctionWithDefault()
        verify(mockSuspendable).suspendClassFunctionWithDefault(2)
    }

    @Test
    fun shouldMockFinalSuspendableClass() = runBlocking<Unit> {
        val mockClass = mock(FinalSuspendableClass::class.java)
        `when`(mockClass.fetch()).thenReturn(10)
        assertThat(mockClass.fetch(), IsEqual(10))
        verify(mockClass).fetch()
    }
}
