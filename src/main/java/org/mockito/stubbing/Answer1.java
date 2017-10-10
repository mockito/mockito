/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Generic interface to be used for configuring mock's answer for a single argument invocation.
 *
 * Answer specifies an action that is executed and a return value that is returned when you interact with the mock.
 * <p>
 * Example of stubbing a mock with this custom answer:
 *
 * <pre class="code"><code class="java">
 * import static org.mockito.AdditionalAnswers.answer;
 *
 * when(mock.someMethod(anyString())).then(answer(
 *     new Answer1&lt;Integer, String&gt;() {
 *         public Integer answer(String arg) {
 *             return arg.length();
 *         }
 * }));
 *
 * //Following will print "3"
 * System.out.println(mock.someMethod("foo"));
 * </code></pre>
 *
 * @param <T> return type
 * @param <A0> type of the single argument
 * @see Answer
 */
@Incubating
public interface Answer1<T, A0> {
    /**
     * @param argument0 the single argument.
     *
     * @return the value to be returned.
     *
     * @throws Throwable the throwable to be thrown
     */
    T answer(A0 argument0) throws Throwable;
}
