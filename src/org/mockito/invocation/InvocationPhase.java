package org.mockito.invocation;

public enum InvocationPhase
{
    /**
     * Invocation occurred defining an invocation (i.e., during a when() call).
     */
    DEFINE,

    /**
     * Invocation occurred executing a method on a mock/spy.
     */
    EXECUTE,

    /**
     * Invocation occurred during a verify() call.
     */
    VERIFY
}
