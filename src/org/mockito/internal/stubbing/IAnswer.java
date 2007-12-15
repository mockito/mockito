/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

/**
 * Used to answer expected calls.
 * @param <T> the type to return.
 */
public interface IAnswer<T> {

    /**
     * @return the value to be returned
     * @throws Throwable the throwable to be thrown
     */
    T answer() throws Throwable;
}
