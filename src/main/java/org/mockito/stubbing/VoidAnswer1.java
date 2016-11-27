/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Generic interface to be used for configuring mock's answer for a single argument invocation that returns nothing.
 *
 * Answer specifies an action that is executed when you interact with the mock.
 * <p>
 * Example of stubbing a mock with this custom answer:
 *
 * <pre class="code"><code class="java">
 * when(mock.someMethod(anyString())).thenAnswer(new Answer&lt;String&gt;() {
 *     void answer(String msg) {
 *         throw new Exception(msg);
 *     }
 * });
 *
 * //Following will raise an exception with the message "boom"
 * mock.someMethod("boom");
 * </code></pre>
 *
 * @param <A0> type of the single argument
 * @see Answer
 */
@Incubating
public interface VoidAnswer1<A0> {
    /**
     * @param argument0 the single argument.
     *
     * @throws Throwable the throwable to be thrown
     */
    void answer(A0 argument0) throws Throwable;
}
