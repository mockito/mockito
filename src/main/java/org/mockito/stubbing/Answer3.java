/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Generic interface to be used for configuring mock's answer for a three argument invocation.
 *
 * Answer specifies an action that is executed and a return value that is returned when you interact with the mock.
 * <p>
 * Example of stubbing a mock with this custom answer:
 *
 * <pre class="code"><code class="java">
 * import static org.mockito.AdditionalAnswers.answer;
 *
 * when(mock.someMethod(anyInt(), anyString(), anyChar())).then(answer(
 *     new Answer3&lt;StringBuilder, Integer, String, Character&gt;() {
 *         public StringBuilder answer(Integer i, String s, Character c) {
 *             return new StringBuilder().append(i).append(s).append(c);
 *         }
 * }));
 *
 * //Following will print "3xyz"
 * System.out.println(mock.someMethod(3, "xy", 'z'));
 * </code></pre>
 *
 * @param <T> return type
 * @param <A0> type of the first argument
 * @param <A1> type of the second argument
 * @param <A2> type of the third argument
 * @see Answer
 */
@Incubating
public interface Answer3<T, A0, A1, A2> {
    /**
     * @param argument0 the first argument.
     * @param argument1 the second argument.
     * @param argument2 the third argument.
     *
     * @return the value to be returned.
     *
     * @throws Throwable the throwable to be thrown
     */
    T answer(A0 argument0, A1 argument1, A2 argument2) throws Throwable;
}
