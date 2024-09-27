/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;

public interface ArgumentMatcherAction {
    /**
     * Implementations must apply the given matcher to the argument and return
     * <code>true</code> if the operation was successful or <code>false</code>
     * if not. In this case no more matchers and arguments will be passed by
     * {@link MatcherApplicationStrategy#forEachMatcherAndArgument(ArgumentMatcherAction)} to this method.
     * .
     *
     * @param matcher
     *            to process the argument, never <code>null</code>
     * @param argument
     *            to be processed by the matcher, can be <code>null</code>
     * @return
     *         <ul>
     *         <li><code>true</code> if the <b>matcher</b> was successfully
     *         applied to the <b>argument</b> and the next pair of matcher and
     *         argument should be passed
     *         <li><code>false</code> otherwise
     *         </ul>
     *
     *
     */
    boolean apply(ArgumentMatcher<?> matcher, Object argument);
}
