/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.Mockito;

/**
 * Stubs with return value or exception. E.g:
 * 
 * <pre>
 * stub(mock.countElements()).andReturn(10);
 * 
 * stub(mock.countElements()).andThrow(new RuntimeException());
 * </pre>
 * 
 * See examples {@link Mockito#stub}
 */
public interface OngoingStubbing<T> {

    /**
     * Stub mock object with given return value. E.g:
     * <pre>
     * stub(mock.countElements()).andReturn(10);
     * </pre>
     * 
     * See examples {@link Mockito#stub}
     * 
     * @param stubbed return value
     */
    void andReturn(T value);

    /**
     * Stub mock object with throwable that will be thrown on method invocation. E.g:
     * <pre>
     * stub(mock.countElements()).andThrow(new RuntimeException());
     * </pre>
     *
     * If throwable is a checked exception then it has to 
     * match one of the checked exceptions of method signature.
     * 
     * See examples {@link Mockito#stub}
     * 
     * @param throwable to be thrown on method invocation
     */
    void andThrow(Throwable throwable);
}