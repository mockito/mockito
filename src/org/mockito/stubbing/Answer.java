/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.invocation.InvocationOnMock;

/**
 * Used to answer expected calls.
 *
 * @param <T> the type to return.
 */
public interface Answer<T> {
    /**
     * @param invocation the invocation on the mock.
     *
     * @return the value to be returned
     *
     * @throws Throwable the throwable to be thrown
     */
    T answer(InvocationOnMock invocation) throws Throwable;
}