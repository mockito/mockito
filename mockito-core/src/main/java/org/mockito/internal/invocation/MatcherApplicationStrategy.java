/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.invocation.Invocation;

public class MatcherApplicationStrategy {

    private final Invocation invocation;
    private final List<? extends ArgumentMatcher<?>> matchers;

    private MatcherApplicationStrategy(
            Invocation invocation, List<? extends ArgumentMatcher<?>> matchers) {
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
            Invocation invocation, List<? extends ArgumentMatcher<?>> matchers) {
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
        final boolean isJavaVarargs =
                invocation.getMethod().isVarArgs()
                        && lastMatcherType().stream()
                                .anyMatch(
                                        matcherType ->
                                                lastParameterType().isAssignableFrom(matcherType));

        // For mockito-scala,
        // we can consider this to be a vararg only if the number of raw arguments is different
        // from the number of arguments and any parameter type is a scala Seq, as Scala represents
        // varargs as Seq and a Scala method can have several parameter lists, which means vararg
        // parameter can be in any position.
        final boolean isScalaVararg =
                invocation.getRawArguments().length != invocation.getArguments().length
                        && optionalClass("scala.collection.Seq").stream()
                                .anyMatch(
                                        seqClass ->
                                                Arrays.stream(
                                                                invocation
                                                                        .getMethod()
                                                                        .getParameterTypes())
                                                        .anyMatch(seqClass::isAssignableFrom));

        final boolean isVararg = isJavaVarargs || isScalaVararg;

        if (invocation.getRawArguments().length == matchers.size() && isVararg) {
            return argsMatch(invocation.getRawArguments(), matchers, action);
        }

        if (invocation.getArguments().length == matchers.size()) {
            return argsMatch(invocation.getArguments(), matchers, action);
        }

        return false;
    }

    private boolean argsMatch(
            Object[] arguments,
            List<? extends ArgumentMatcher<?>> matchers,
            ArgumentMatcherAction action) {
        for (int i = 0; i < arguments.length; i++) {
            ArgumentMatcher<?> matcher = matchers.get(i);
            Object argument = arguments[i];

            if (!action.apply(matcher, argument)) {
                return false;
            }
        }
        return true;
    }

    private Optional<Class<?>> lastMatcherType() {
        return matchers.isEmpty()
                ? Optional.empty()
                : Optional.of(matchers.get(matchers.size() - 1).type());
    }

    private Class<?> lastParameterType() {
        final Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
        return parameterTypes[parameterTypes.length - 1];
    }

    private Optional<Class<?>> optionalClass(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
