/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

public enum StubbingStyle {
    /**
     * The mock invocation precedes the answer, as in {@code when(...).thenReturn(...)}
     * or {@code given(...).willReturn(...)}.
     */
    INVOCATION_FIRST,

    /**
     * The answer precedes the mock invocation, as in {@code doReturn(...).when(...).method()}
     * or {@code willReturn(...).given(...).method()}.
     */
    ANSWER_FIRST
}
