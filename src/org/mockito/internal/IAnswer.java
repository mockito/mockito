/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

/**
 * Used to answer expected calls.
 * @param <T> the type to return.
 */
public interface IAnswer<T> {
    /**
     * is called to answer an expected call. 
     * The answer may be to return a value, or to throw an exception.
     * The arguments of the call for which the answer is generated 
     * are available via getCurrentArguments() - be careful
     * here, using the arguments is not refactoring-safe.
     * 
     * @return the value to be returned
     * @throws Throwable the throwable to be thrown
     */
    T answer() throws Throwable;
}
