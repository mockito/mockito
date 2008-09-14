/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

/**
 * Simply put: "<b>When</b> the x method is called <b>then</b> return y". E.g:
 *
 * <pre>
 * <b>when</b>(mock.someMethod()).<b>thenReturn</b>(10);
 *
 * //you can use flexible argument matchers, e.g:
 * when(mock.someMethod(<b>anyString()</b>)).thenReturn(10);
 *
 * //setting exception to be thrown:
 * when(mock.someMethod("some arg")).thenThrow(new RuntimeException());
 *
 * //you can set different behavior for consecutive method calls.
 * //Last stubbing (e.g: thenReturn("foo")) determines the behavior of further consecutive calls.
 * when(mock.someMethod("some arg"))
 *  .thenThrow(new RuntimeException())
 *  .thenReturn("foo");
 *
 * </pre>
 *
 * See examples in javadoc for {@link Mockito#when}
 */
public interface NewOngoingStubbing<T> {

    /**
     * Sets a return value to be returned when the method is called. E.g:
     * <pre>
     * when(mock.someMethod()).thenReturn(10);
     * </pre>
     *
     * See examples in javadoc for {@link Mockito#when}
     *
     * @param value return value
     *
     * @return ongoingStubbing object that allows stubbing consecutive calls
     */
    NewOngoingStubbing<T> thenReturn(T value);

    /**
     * Sets a Throwable to be thrown when the method is called. E.g:
     * <pre>
     * when(mock.someMethod()).thenThrow(new RuntimeException());
     * </pre>
     *
     * If throwable is a checked exception then it has to
     * match one of the checked exceptions of method signature.
     *
     * See examples in javadoc for {@link Mockito#when}
     *
     * @param throwable to be thrown on method invocation
     *
     * @return ongoingStubbing object that allows stubbing consecutive calls
     */
    NewOngoingStubbing<T> thenThrow(Throwable throwable);

    /**
     * Sets a generic Answer for the method. E.g:
     * <pre>
     * when(mock.someMethod(10)).thenAnswer(new Answer&lt;Integer&gt;() {
     *     public Integer answer(InvocationOnMock invocation) throws Throwable {
     *         return (Integer) invocation.getArguments()[0];
     *     }
     * }
     * </pre>
     *
     * @param answer the custom answer to execute.
     *
     * @return ongoingStubbing object that allows stubbing consecutive calls
     */
    NewOngoingStubbing<T> thenAnswer(Answer<?> answer);
}