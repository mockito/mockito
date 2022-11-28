/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

/**
 * Internal interface that informs Mockito that the matcher is intended to capture varargs.
 * This information is needed when mockito collects the arguments.
 */
public interface VarargMatcher extends Serializable {

    /**
     * The type of the argument the matcher matches.
     *
     * <p>If a vararg aware matcher:
     * <ul>
     *     <li>is at the parameter index of a vararg parameter</li>
     *     <li>is the last matcher passed</li>
     *     <li>matchers the raw type of the vararg parameter</li>
     * </ul>
     *
     * Then the matcher is matched against the vararg raw parameter.
     * Otherwise, the matcher will be matched against each element in the vararg raw parameters.
     *
     * <p>For example:
     *
     * <pre class="code"><code class="java">
     *  // Given vararg method with signature:
     *  int someVarargMethod(int x, String... args);
     *
     *  // The following will match the last matcher against the contents of the `args` array:
     *  (as the above criteria are met)
     *  mock.someVarargMethod(eq(1), any(String[].class));
     *
     *  // The following will match the last matcher against each element of the `args` array:
     *  // (as the type of the last matcher does not match the raw type of the vararg parameter)
     *  mock.someVarargMethod(eq(1), any(String.class));
     *
     *  // The following will match only invocations with two strings in the 'args' array:
     *  // (as there are more matchers than raw arguments)
     *  mock.someVarargMethod(eq(1), any(), any());
     * </code></pre>
     *
     * @return the type this matcher handles.
     * @since 4.10.0
     */
    default Class<?> type() {
        return Void.class;
    }
}
