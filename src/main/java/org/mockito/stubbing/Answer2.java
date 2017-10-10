/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Generic interface to be used for configuring mock's answer for a two argument invocation.
 *
 * Answer specifies an action that is executed and a return value that is returned when you interact with the mock.
 * <p>
 * Example of stubbing a mock with this custom answer:
 *
 * <pre class="code"><code class="java">
 * import static org.mockito.AdditionalAnswers.answer;
 *
 * when(mock.someMethod(anyString(), anyChar())).then(answer(
 *     new Answer2&lt;String, String, Character&gt;() {
 *         public String answer(String s, Character c) {
 *             return s.replace('f', c);
 *         }
 * }));
 *
 * //Following will print "bar"
 * System.out.println(mock.someMethod("far", 'b'));
 * </code></pre>
 *
 * @param <T> return type
 * @param <A0> type of the first argument
 * @param <A1> type of the second argument
 * @see Answer
 */
@Incubating
public interface Answer2<T, A0, A1> {
    /**
     * @param argument0 the first argument.
     * @param argument1 the second argument.
     *
     * @return the value to be returned.
     *
     * @throws Throwable the throwable to be thrown
     */
    T answer(A0 argument0, A1 argument1) throws Throwable;
}
