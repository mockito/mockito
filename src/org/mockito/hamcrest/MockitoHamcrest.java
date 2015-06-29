package org.mockito.hamcrest;

import org.hamcrest.Matcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.ArgumentMatcher;

/**
 * Allows matching arguments with hamcrest matchers.
 * <p>
 * Before implementing or reusing an existing hamcrest matcher please read
 * how to deal with sophisticated argument matching in {@link ArgumentMatcher}.
 * <p>
 * Mockito 2.0 was decoupled from Hamcrest to avoid version incompatibilities
 * that have impacted our users in past. Mockito offers a dedicated API to match arguments
 * via {@link ArgumentMatcher}.
 * Hamcrest integration is provided so that users can take advantage of existing Hamcrest matchers.
 *
 * Example:
 * <pre>
 *     import static org.mockito.hamcrest.MockitoHamcrest.argThat;
 *
 *     //stubbing
 *     when(mock.giveMe(argThat(new MyHamcrestMatcher())));
 *
 *     //verification
 *     verify(mock).giveMe(argThat(new MyHamcrestMatcher()));
 * </pre>
 *
 * @since 2.0
 */
public class MockitoHamcrest {

    private static final MockingProgress MOCKING_PROGRESS = new ThreadSafeMockingProgress();

    /**
     * Allows matching arguments with hamcrest matchers.
     * <p>
     * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     *
     * @return <code>null</code> or default value for primitive (0, false, etc.)
     *
     * @since 2.0
     */
    public static <T> T argThat(Matcher<T> matcher) {
        return (T) MOCKING_PROGRESS.getArgumentMatcherStorage()
                .reportMatcher(new HamcrestArgumentMatcher(matcher))
                .returnFor(MatcherGenericTypeExtractor.genericTypeOfMatcher(matcher.getClass()));
    }
}
