/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Generic interface to be used for configuring mock's answer for a four argument invocation that returns nothing.
 *
 * Answer specifies an action that is executed when you interact with the mock.
 * <p>
 * Example of stubbing a mock with this custom answer:
 *
 * <pre class="code"><code class="java">
 * when(mock.someMethod(anyString(), anyInt(), anyString(), anyChar())).thenAnswer(
 *     new Answer&lt;String, Integer, String, Character&gt;() {
 *         void answer(String msg, Integer count, String another, Character c) {
 *             throw new Exception(String.format(msg, another, c, count));
 *         }
 *     });
 *
 * //Following will raise an exception with the message "ka-boom <3"
 * mock.someMethod("%s-boom %c%d", 3, "ka", '&lt;');
 * </code></pre>
 *
 * @param <A0> type of the first argument
 * @param <A1> type of the second argument
 * @param <A2> type of the third argument
 * @param <A3> type of the fourth argument
 * @see Answer
 */
@Incubating
public interface VoidAnswer4<A0, A1, A2, A3> {
    /**
     * @param argument0 the first argument.
     * @param argument1 the second argument.
     * @param argument2 the third argument.
     * @param argument3 the fourth argument.
     *
     * @throws Throwable the throwable to be thrown
     */
    void answer(A0 argument0, A1 argument1, A2 argument2, A3 argument3) throws Throwable;
}
