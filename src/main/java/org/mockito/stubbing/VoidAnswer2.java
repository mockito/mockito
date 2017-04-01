/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Generic interface to be used for configuring mock's answer for a two argument invocation that returns nothing.
 *
 * Answer specifies an action that is executed when you interact with the mock.
 * <p>
 * Example of stubbing a mock with this custom answer:
 *
 * <pre class="code"><code class="java">
 * import static org.mockito.AdditionalAnswers.answerVoid;
 *
 * doAnswer(answerVoid(
 *     new VoidAnswer2&lt;String, Integer&gt;() {
 *         public void answer(String msg, Integer count) throws Exception {
 *             throw new Exception(String.format(msg, count));
 *         }
 * })).when(mock).someMethod(anyString(), anyInt());
 *
 * //Following will raise an exception with the message "boom 3"
 * mock.someMethod("boom %d", 3);
 * </code></pre>
 *
 * @param <A0> type of the first argument
 * @param <A1> type of the second argument
 * @see Answer
 */
@Incubating
public interface VoidAnswer2<A0, A1> {
    /**
     * @param argument0 the first argument.
     * @param argument1 the second argument.
     *
     * @throws Throwable the throwable to be thrown
     */
    void answer(A0 argument0, A1 argument1) throws Throwable;
}
