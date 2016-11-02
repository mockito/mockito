package org.mockito.invocation;

import org.mockito.ArgumentMatcher;

import java.util.List;

/**
 * TODO Javadoc, include use case, @since tags
 */
public interface MatchableInvocation extends DescribedInvocation {

    /**
     * TODO Javadoc, include use case, @since tags
     */
    Invocation getInvocation();

    /**
     * TODO Javadoc, include use case, @since tags
     */
    List<ArgumentMatcher> getMatchers();

    /**
     * TODO Javadoc, include use case, @since tags
     */
    boolean matches(Invocation actual);

    /**
     * TODO Javadoc, include use case, @since tags
     */
    boolean hasSimilarMethod(Invocation candidate);

    /**
     * TODO Javadoc, include use case, @since tags
     */
    boolean hasSameMethod(Invocation candidate);

    /**
     * This method is used by Mockito to implement argument captor functionality (see {@link org.mockito.ArgumentCaptor}.
     * <p>
     * Makes this instance of matchable invocation capture all arguments of provided invocation.
     *
     * @param invocation the invocation to capture the arguments from
     *
     * TODO @since
     */
    void captureArgumentsFrom(Invocation invocation);
}
