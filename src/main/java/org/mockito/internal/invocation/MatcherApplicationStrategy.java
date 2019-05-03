/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.mockito.internal.invocation.MatcherApplicationStrategy.MatcherApplicationType.ERROR_UNSUPPORTED_NUMBER_OF_MATCHERS;
import static org.mockito.internal.invocation.MatcherApplicationStrategy.MatcherApplicationType.MATCH_EACH_VARARGS_WITH_LAST_MATCHER;
import static org.mockito.internal.invocation.MatcherApplicationStrategy.MatcherApplicationType.ONE_MATCHER_PER_ARGUMENT;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;

public class MatcherApplicationStrategy {

    private final Invocation invocation;
    private final List<ArgumentMatcher<?>> matchers;
    private final MatcherApplicationType matchingType;



    private MatcherApplicationStrategy(Invocation invocation, List<ArgumentMatcher<?>> matchers, MatcherApplicationType matchingType) {
        this.invocation = invocation;
        if (matchingType == MATCH_EACH_VARARGS_WITH_LAST_MATCHER) {
            int times = varargLength(invocation);
            this.matchers = appendLastMatcherNTimes(matchers, times);
        } else {
            this.matchers = matchers;
        }

        this.matchingType = matchingType;
    }

    /**
     * Returns the {@link MatcherApplicationStrategy} that must be used to capture the
     * arguments of the given <b>invocation</b> using the given <b>matchers</b>.
     *
     * @param invocation
     *            that contain the arguments to capture
     * @param matchers
     *            that will be used to capture the arguments of the invocation,
     *            the passed {@link List} is not required to contain a
     *            {@link CapturingMatcher}
     * @return never <code>null</code>
     */
    public static MatcherApplicationStrategy getMatcherApplicationStrategyFor(Invocation invocation, List<ArgumentMatcher<?>> matchers) {

        MatcherApplicationType type = getMatcherApplicationType(invocation, matchers);
        return new MatcherApplicationStrategy(invocation, matchers, type);
    }

    /**
     * Applies the given {@link ArgumentMatcherAction} to all arguments and
     * corresponding matchers
     *
     * @param action
     *            must not be <code>null</code>
     * @return
     *         <ul>
     *         <li><code>true</code> if the given <b>action</b> returned
     *         <code>true</code> for all arguments and matchers passed to it.
     *         <li><code>false</code> if the given <b>action</b> returned
     *         <code>false</code> for one of the passed arguments and matchers
     *         <li><code>false</code> if the given matchers don't fit to the given invocation
     *         because too many or to few matchers are available.
     *         </ul>
     */
    public boolean forEachMatcherAndArgument(ArgumentMatcherAction action) {
        if (matchingType == ERROR_UNSUPPORTED_NUMBER_OF_MATCHERS)
            return false;

        Object[] arguments = invocation.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            ArgumentMatcher<?> matcher = matchers.get(i);
            Object argument = arguments[i];

            if (!action.apply(matcher, argument)) {
                return false;
            }
        }
        return true;
    }

    private static MatcherApplicationType getMatcherApplicationType(Invocation invocation, List<ArgumentMatcher<?>> matchers) {
        final int rawArguments = invocation.getRawArguments().length;
        final int expandedArguments = invocation.getArguments().length;
        final int matcherCount = matchers.size();

        if (expandedArguments == matcherCount) {
            return ONE_MATCHER_PER_ARGUMENT;
        }

        if (rawArguments == matcherCount && isLastMatcherVarargMatcher(matchers)) {
            return MATCH_EACH_VARARGS_WITH_LAST_MATCHER;
        }

        return ERROR_UNSUPPORTED_NUMBER_OF_MATCHERS;
    }

    private static boolean isLastMatcherVarargMatcher(final List<ArgumentMatcher<?>> matchers) {
        ArgumentMatcher<?> argumentMatcher = lastMatcher(matchers);
        if (argumentMatcher instanceof HamcrestArgumentMatcher<?>) {
           return  ((HamcrestArgumentMatcher<?>) argumentMatcher).isVarargMatcher();
        }
        return argumentMatcher instanceof VarargMatcher;
    }

    private static List<ArgumentMatcher<?>> appendLastMatcherNTimes(List<ArgumentMatcher<?>> matchers, int timesToAppendLastMatcher) {
        ArgumentMatcher<?> lastMatcher = lastMatcher(matchers);

        List<ArgumentMatcher<?>> expandedMatchers = new ArrayList<ArgumentMatcher<?>>(matchers);
        for (int i = 0; i < timesToAppendLastMatcher; i++) {
            expandedMatchers.add(lastMatcher);
        }
        return expandedMatchers;
    }

    private static int varargLength(Invocation invocation) {
        int rawArgumentCount = invocation.getRawArguments().length;
        int expandedArgumentCount = invocation.getArguments().length;
        return expandedArgumentCount - rawArgumentCount;
    }

    private static ArgumentMatcher<?> lastMatcher(List<ArgumentMatcher<?>> matchers) {
        return matchers.get(matchers.size() - 1);
    }

    enum MatcherApplicationType {
        ONE_MATCHER_PER_ARGUMENT, MATCH_EACH_VARARGS_WITH_LAST_MATCHER, ERROR_UNSUPPORTED_NUMBER_OF_MATCHERS;
    }
}
