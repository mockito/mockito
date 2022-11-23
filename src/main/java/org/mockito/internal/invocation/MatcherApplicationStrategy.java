/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;

public class MatcherApplicationStrategy {

    private final Invocation invocation;
    private final List<ArgumentMatcher<?>> matchers;

    private MatcherApplicationStrategy(
            Invocation invocation,
            List<ArgumentMatcher<?>> matchers) {
        this.invocation = invocation;
        this.matchers = matchers;
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
    public static MatcherApplicationStrategy getMatcherApplicationStrategyFor(
            Invocation invocation, List<ArgumentMatcher<?>> matchers) {

        return new MatcherApplicationStrategy(invocation, matchers);
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
        final boolean isVararg = invocation.getMethod().isVarArgs()
            && invocation.getRawArguments().length == matchers.size()
            && getLastVarargMatcher(matchers).isPresent();

        if (isVararg) {
            final Type type = getLastVarargMatcher(matchers).get().type();
            final Class<?> varArgType = invocation.getMethod().getParameterTypes()[invocation.getMethod().getParameterTypes().length - 1];
            if (type.equals(varArgType)) {
                return argsMatch(invocation.getRawArguments(), matchers, action);
            }
        }

        if (invocation.getArguments().length == matchers.size()) {
            return argsMatch(invocation.getArguments(), matchers, action);
        }

        if (isVararg) {
            int times = varargLength(invocation);
            final List<ArgumentMatcher<?>> matchers = appendLastMatcherNTimes(this.matchers, times);
            return argsMatch(invocation.getArguments(), matchers, action);
        }

        return false;
    }

    private boolean argsMatch(final Object[] arguments,
                              final List<ArgumentMatcher<?>> matchers,
                              final ArgumentMatcherAction action) {

        for (int i = 0; i < arguments.length; i++) {
            ArgumentMatcher<?> matcher = matchers.get(i);
            Object argument = arguments[i];

            if (!action.apply(matcher, argument)) {
                return false;
            }
        }
        return true;
    }

    private static Optional<VarargMatcher> getLastVarargMatcher(final List<ArgumentMatcher<?>> matchers) {
        ArgumentMatcher<?> argumentMatcher = lastMatcher(matchers);
        if (argumentMatcher instanceof HamcrestArgumentMatcher<?>) {
            return ((HamcrestArgumentMatcher<?>) argumentMatcher).varargMatcher();
        }
        return argumentMatcher instanceof VarargMatcher ? Optional.of((VarargMatcher) argumentMatcher) : Optional.empty();
    }

    private static List<ArgumentMatcher<?>> appendLastMatcherNTimes(
            List<ArgumentMatcher<?>> matchers, int timesToAppendLastMatcher) {
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
}
