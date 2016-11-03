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
 * @since @since@
 */
public interface MatchableInvocation extends DescribedInvocation {

    /**
     * The actual invocation Mockito will match against.
     *
     * @since @since@
     */
    Invocation getInvocation();

    /**
     * The argument matchers of this invocation.
     * When the invocation is declared without argument matchers (e.g. using plain arguments)
     * Mockito still converts them into {@link ArgumentMatcher} instances
     * that use 'eq' matching via {@link org.mockito.Mockito#eq(Object)}.
     *
     * @since @since@
     */
    List<ArgumentMatcher> getMatchers();

    /**
     * Same method, mock and all arguments match.
     *
     * @since @since@
     */
    boolean matches(Invocation candidate);

    /**
     * Candidate invocation has the similar method.
     * 'Similar' means the same method name, same mock, unverified, not overloaded, but not necessarily matching arguments
     *
     * @since @since@
     */
    boolean hasSimilarMethod(Invocation candidate);

    /**
     * Returns true if the candidate invocation has the same method (method name and parameter types)
     *
     * @since @since@
     */
    boolean hasSameMethod(Invocation candidate);

    /**
     * This method is used by Mockito to implement argument captor functionality (see {@link org.mockito.ArgumentCaptor}.
     * <p>
     * Makes this instance of matchable invocation capture all arguments of provided invocation.
     *
     * @param invocation the invocation to capture the arguments from
     *
     * @since @since@
     */
    void captureArgumentsFrom(Invocation invocation);
}
