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
public interface VoidMethodStubbable<T> extends StubbedMethodSelector<T>{

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
    VoidMethodStubbable<T> toThrow(Throwable throwable);
    
    /**
     * Stubs void method to 'just return' (e.g. to <b>not</b> throw any exception)
     * <p>
     * <b>Only</b> use this method if you're chaining multiple return values.
     * <p>
     * For example:
     * <pre>
     * stubVoid(mock)
     *   .toReturn()
     *   .toThrow(new RuntimeException())
     *   .on().foo(10);
     * </pre>
     * <ol> 
     * <li>first time foo(10) is called the mock will 'just return' (e.g. don't throw any exception)</li>
     * <li>second time foo(10) is called the mock will throw RuntimeException</li>
     * <li>every next time foo(10) is called the mock will throw RuntimeException</li>
     * </ol> 
     * 
     * See examples in javadoc for {@link Mockito#stubVoid}
     * 
     * @param throwable
     *            to be thrown on method invocation
     * 
     * @return method selector - to choose void method and finish stubbing 
     */
    VoidMethodStubbable<T> toReturn();

}