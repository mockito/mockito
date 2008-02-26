/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.Mockito;

/**
 * Stubs void method with an exception. E.g:
 * 
 * <pre>
 * stubVoid(mock).toThrow(new RuntimeException()).on().someMethod();
 * </pre>
 * 
 * See examples in javadoc for {@link Mockito#stubVoid}
 */
public interface VoidMethodStubbable<T> {

    /**
     * Stubs void method with an exception. E.g:
     * 
     * <pre>
     * stubVoid(mock).toThrow(new RuntimeException()).on().someMethod();
     * </pre>
     * 
     * If throwable is a checked exception then it has to 
     * match one of the checked exceptions of method signature.
     * 
     * See examples in javadoc for {@link Mockito#stubVoid}
     * 
     * @param throwable
     *            to be thrown on method invocation
     * 
     * @return method selector - to choose void method and finish stubbing 
     */
    StubbedMethodSelector<T> toThrow(Throwable throwable);

}