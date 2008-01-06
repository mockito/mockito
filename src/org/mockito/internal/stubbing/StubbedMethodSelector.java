/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.Mockito;

/**
 * Chooses void method for stubbing with throwable. E.g:
 * 
 * <pre>
 * stubVoid(mock).toThrow(new RuntimeException()).on().someMethod("some arg");
 * </pre>
 * 
 * See examples in javadoc for {@link Mockito#stubVoid}
 */
public interface StubbedMethodSelector<T> {

    /**
     * Choose void method for stubbing with throwable. E.g:
     * 
     * <pre>
     * stubVoid(mock).toThrow(new RuntimeException()).on().someMethod("some arg");
     * </pre>
     * 
     * If throwable is a checked exception then it has to match one of the
     * checked exceptions of method signature.
     * <p>
     * See examples in javadoc for {@link Mockito#stubVoid}
     * 
     * @return mock object itself
     */
    T on();
}