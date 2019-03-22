/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Incubating;

/**
 * Generic interface to be used for configuring mock's answer for a six argument invocation.
 *
 * Answer specifies an action that is executed and a return value that is returned when you interact with the mock.
 * <p>
 * Example of stubbing a mock with this custom answer:
 *
 * <pre class="code"><code class="java">
 * import static org.mockito.AdditionalAnswers.answer;
 *
 * when(mock.someMethod(anyInt(), anyString(), anyChar(), any(), any(), anyBoolean())).then(answer(
 *     new Answer6&lt;StringBuilder, Integer, String, Character, Object, Object, Boolean&gt;() {
 *         public StringBuilder answer(Integer i, String s, Character c, Object o1, Object o2, Boolean isIt) {
 *             return new StringBuilder().append(i).append(s).append(c).append(o1.hashCode()).append(o2.hashCode()).append(isIt);
 *         }
 * }));
 *
 * //Following will print a string like "3xyz131635550true"
 * System.out.println(mock.someMethod(3, "xy", 'z', new Object(), new Object(), true));
 * </code></pre>
 *
 * @param <T> return type
 * @param <A0> type of the first argument
 * @param <A1> type of the second argument
 * @param <A2> type of the third argument
 * @param <A3> type of the fourth argument
 * @param <A4> type of the fifth argument
 * @param <A5> type of the sixth argument
 * @see Answer
 */
@Incubating
public interface Answer6<T, A0, A1, A2, A3, A4, A5> {
    /**
     * @param argument0 the first argument.
     * @param argument1 the second argument.
     * @param argument2 the third argument.
     * @param argument3 the fourth argument.
     * @param argument4 the fifth argument.
     * @param argument5 the sixth argument.
     *
     * @return the value to be returned.
     *
     * @throws Throwable the throwable to be thrown
     */
    T answer( A0 argument0, A1 argument1, A2 argument2, A3 argument3, A4 argument4, A5 argument5 ) throws Throwable;
}
