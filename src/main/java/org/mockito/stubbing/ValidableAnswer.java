/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.stubbing;

import org.mockito.Incubating;
import org.mockito.invocation.InvocationOnMock;

/**
 * Allow to validate this answer is correct for the given invocation.
 *
 * <p>
 * When tests use a shared answer implementation, it may happen the answer cannot be used
 * with some methods. Implementing this interface indicate to Mockito it needs to verify the answer is compatible
 * with the stubbed interaction.
 * </p>
 *
 * <p>
 * In the following example the answer is shared and work as expected...
 *
 * <pre class="code"><code class="java">
 * when(mock.someMethod(anyString())).then(doSomethingTricky());
 *
 * static Answer doSomethingTricky() {
 *     return new Answer() {
 *         Object answer(InvocationOnMock invocation) {
 *             // tricky stuff
 *         }
 *     });
 * }
 * </code></pre>
 * </p>
 *
 * <p>
 * ...then later there's some refactoring or some new code that want to use the answer,
 * however it is not compatible anymore. In this example the answer may throw an exception because
 * the Answer cannot work with the return type or some parameter types.
 *
 * <pre class="code"><code class="java">
 * when(mock.someMethod(anyString(), anyInt())).then(doSomethingTricky()); // fail at answer execution time
 * when(mock.incompatibleMethod(anyVararg())).then(doSomethingTricky()); // fail at answer execution time
 * </code></pre>
 * </p>
 *
 * <p>
 * Instead of having an exception raised later at answer <em>execution time</em>, one can make this answer
 * validable at <em>stub time</em> by implementing this contract.
 *
 * <pre class="code"><code class="java">
 * when(mock.incompatibleMethod(anyVararg())).then(doSomethingTricky()); // fail at answer stub time
 *
 * static Answer doSomethingTricky() {
 *     return new TrickyAnswer();
 * }
 *
 * class Tricky Answer implements Answer, ValidableAnswer {
 *     public Object answer(InvocationOnMock invocation) {
 *         // tricky stuff
 *     }
 *
 *     public void validateFor(InvocationOnMock invocation) {
 *         // perform validation for this interaction
 *     }
 * }
 * </code></pre>
 * </p>
 *
 * @since 2.3.8
 */
@Incubating
public interface ValidableAnswer {

    /**
     * Validation of the answer at <em>stub time</em> for the given invocation.
     *
     * <p>
     * This method will be called by Mockito.
     * </p>
     *
     * <p>
     * The implementation must throw an MockitoException to indicate that this answer is not valid for
     * the given invocation. If the validation succeed the implementation must simply return without throwing.
     * </p>
     *
     * @param invocation The stubbed invocation
     *
     * @since 2.3.8
     */
    void validateFor(InvocationOnMock invocation);

}
