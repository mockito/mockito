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
 * stub(mock.someMethod()).toReturn(10);
 * 
 * //you can use flexible argument matchers, e.g:
 * stub(mock.someMethod(<b>anyString()</b>)).toReturn(10);
 * 
 * //setting exception to be thrown:
 * stub(mock.someMethod("some arg")).toThrow(new RuntimeException());
 * </pre>
 * 
 * See examples in javadoc for {@link Mockito#stub}
 */
public interface OngoingStubbing<T> {

    /**
     * Stub mock object with given return value. E.g:
     * <pre>
     * stub(mock.someMethod()).toReturn(10);
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito#stub}
     * 
     * @param value return value
     */
    void toReturn(T value);

    /**
     * Stub mock object with throwable that will be thrown on method invocation. E.g:
     * <pre>
     * stub(mock.someMethod()).toThrow(new RuntimeException());
     * </pre>
     *
     * If throwable is a checked exception then it has to 
     * match one of the checked exceptions of method signature.
     * 
     * See examples in javadoc for {@link Mockito#stub}
     * 
     * @param throwable to be thrown on method invocation
     */
    void toThrow(Throwable throwable);
}