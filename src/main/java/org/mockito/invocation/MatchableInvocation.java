package org.mockito.invocation;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.invocation.CapturesArgumentsFromInvocation;

import java.util.List;

/**
 * TODO Javadoc, include use case, @since tags
 */
public interface MatchableInvocation extends DescribedInvocation, CapturesArgumentsFromInvocation {
    Invocation getInvocation();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    List<ArgumentMatcher> getMatchers();

    boolean matches(Invocation actual);

    boolean hasSimilarMethod(Invocation candidate);

    boolean hasSameMethod(Invocation candidate);
}
