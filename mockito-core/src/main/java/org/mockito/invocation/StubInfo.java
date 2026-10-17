/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

/**
 * The information about stubbing, for example the location of stubbing.
 */
public interface StubInfo {

    /**
     * @return the location where the invocation was stubbed.
     */
    Location stubbedAt();

    /**
     * Returns the number of times the stubbing that produced this {@code StubInfo}
     * has answered, including the invocation that carries this {@code StubInfo}.
     *
     * <p>
     * See {@link InvocationOnMock#getInvocationCount()} for the full contract.
     *
     * @return the 1-based count of invocations matched by this stubbing, or
     *         {@code 0} if the count is not available
     * @since 5.25.0
     */
    default int getInvocationCount() {
        return 0;
    }
}
