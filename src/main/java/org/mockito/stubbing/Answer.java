/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.invocation.InvocationOnMock;

/**
 * Generic interface to be used for configuring mock's answer. 
 * Answer specifies an action that is executed and a return value that is returned when you interact with the mock.   
 * <p>
 * Example of stubbing a mock with custom answer: 
 * 
 * <pre class="code"><code class="java">
 * when(mock.someMethod(anyString())).thenAnswer(new Answer() {
 *     Object answer(InvocationOnMock invocation) {
 *         Object[] args = invocation.getArguments();
 *         Object mock = invocation.getMock();
 *         return "called with arguments: " + Arrays.toString(args);
 *     }
 * });
 * 
 * //Following prints "called with arguments: [foo]"
 * System.out.println(mock.someMethod("foo"));
 * </code></pre>
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