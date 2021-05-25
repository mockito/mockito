/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Generic interface to be used for configuring mock's answer for a five argument invocation that returns nothing.
 *
 * Answer specifies an action that is executed when you interact with the mock.
 * <p>
 * Example of stubbing a mock with this custom answer:
 *
 * <pre class="code"><code class="java">
 * import static org.mockito.AdditionalAnswers.answerVoid;
 *
 * doAnswer(answerVoid(
 *     new VoidAnswer5&lt;String, Integer, String, Character, String&gt;() {
 *         public void answer(String msg, Integer count, String another, Character c, String subject) throws Exception {
 *             throw new Exception(String.format(msg, another, c, count, subject));
 *         }
 * })).when(mock).someMethod(anyString(), anyInt(), anyString(), anyChar(), anyString());
 *
 * //Following will raise an exception with the message "ka-boom <3 mockito"
 * mock.someMethod("%s-boom %c%d %s", 3, "ka", '&lt;', "mockito");
 * </code></pre>
 *
 * @param <A0> type of the first argument
 * @param <A1> type of the second argument
 * @param <A2> type of the third argument
 * @param <A3> type of the fourth argument
 * @param <A4> type of the fifth argument
 * @see Answer
 */
@Incubating
public interface VoidAnswer5<A0, A1, A2, A3, A4> {
    /**
     * @param argument0 the first argument.
     * @param argument1 the second argument.
     * @param argument2 the third argument.
     * @param argument3 the fourth argument.
     * @param argument4 the fifth argument.
     *
     * @throws Throwable the throwable to be thrown
     */
    void answer(A0 argument0, A1 argument1, A2 argument2, A3 argument3, A4 argument4)
            throws Throwable;
}
