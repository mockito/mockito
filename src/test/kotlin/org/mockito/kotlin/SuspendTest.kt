package org.mockito.kotlin

import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Test for mocking and verifying Kotlin suspending functions.
 */
class SuspendTest {
    interface Suspendable {
        suspend fun suspendFunction(x: Int): Int
    }

    class CallsSuspendable(val suspendable: Suspendable) {
        fun callSuspendable(x: Int) = runBlocking {
            suspendable.suspendFunction(x)
        }
    }

    @Test
    fun shouldMockSuspendFunction() = runBlocking<Unit> {
        val mockSuspendable: Suspendable = mock(Suspendable::class.java)
        val callsSuspendable = CallsSuspendable(mockSuspendable)

        `when`(mockSuspendable.suspendFunction(1)).thenReturn(2)
        `when`(mockSuspendable.suspendFunction(intThat { it >= 2 })).thenReturn(4)

        assertThat(callsSuspendable.callSuspendable(1), IsEqual(2))
        assertThat(callsSuspendable.callSuspendable(2), IsEqual(4))

        verify(mockSuspendable).suspendFunction(1)
        verify(mockSuspendable).suspendFunction(2)
    }
}
