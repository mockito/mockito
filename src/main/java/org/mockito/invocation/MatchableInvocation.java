/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.ArgumentMatcher;

import java.util.List;

/**
 * <code>MatchableInvocation</code> wraps {@link Invocation} instance
 * and holds argument matchers associated with that invocation.
 * It is used during verification process:
 *
 * <pre class="code"><code class="java">
 *   mock.foo();   // <- invocation
 *   verify(mock).bar();  // <- matchable invocation
 * </code></pre>
 *
 * @since 2.2.12
 */
public interface MatchableInvocation extends DescribedInvocation {

    /**
     * The actual invocation Mockito will match against.
     *
     * @since 2.2.12
     */
    Invocation getInvocation();

    /**
     * The argument matchers of this invocation.
     * When the invocation is declared without argument matchers (e.g. using plain arguments)
     * Mockito still converts them into {@link ArgumentMatcher} instances
     * that use 'eq' matching via {@link org.mockito.Mockito#eq(Object)}.
     *
     * @since 2.2.12
     */
    List<ArgumentMatcher> getMatchers();

    /**
     * Same method, mock and all arguments match.
     *
     * @since 2.2.12
     */
    boolean matches(Invocation candidate);

    /**
     * Candidate invocation has the similar method.
     * 'Similar' means the same method name, same mock, unverified, not overloaded, but not necessarily matching arguments
     *
     * @since 2.2.12
     */
    boolean hasSimilarMethod(Invocation candidate);

    /**
     * Returns true if the candidate invocation has the same method (method name and parameter types)
     *
     * @since 2.2.12
     */
    boolean hasSameMethod(Invocation candidate);

    /**
     * This method is used by Mockito to implement argument captor functionality (see {@link org.mockito.ArgumentCaptor}.
     * <p>
     * Makes this instance of matchable invocation capture all arguments of provided invocation.
     *
     * @param invocation the invocation to capture the arguments from
     *
     * @since 2.2.12
     */
    void captureArgumentsFrom(Invocation invocation);
}
