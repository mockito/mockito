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
 * //There is a shorter way of consecutive stubbing:
 * when(mock.someMethod()).thenReturn(1,2,3);
 * when(mock.otherMethod()).thenThrow(exc1, exc2);
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
     * Sets consecutive return values to be returned when the method is called. E.g:
     * <pre>
     * when(mock.someMethod()).thenReturn(1, 2, 3);
     * </pre>
     *
     * Last return value in the sequence (in example: 3) determines the behavior of further consecutive calls.
     * <p>
     * See examples in javadoc for {@link Mockito#when}
     *
     * @param value first return value
     * @param values next return values
     *
     * @return ongoingStubbing object that allows stubbing consecutive calls
     */
    NewOngoingStubbing<T> thenReturn(T value, T... values);

    /**
     * Sets Throwable objects to be thrown when the method is called. E.g:
     * <pre>
     * when(mock.someMethod()).thenThrow(new RuntimeException());
     * </pre>
     *
     * If throwables contain a checked exception then it has to
     * match one of the checked exceptions of method signature.
     * <p>
     * You can specify throwables to be thrown for consecutive calls. 
     * In that case the last throwable determines the behavior of further consecutive calls.
     * <p>
     * if throwable is null then exception will be thrown.
     * <p>
     * See examples in javadoc for {@link Mockito#when}
     *
     * @param throwables to be thrown on method invocation
     *
     * @return ongoingStubbing object that allows stubbing consecutive calls
     */
    NewOngoingStubbing<T> thenThrow(Throwable... throwables);

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