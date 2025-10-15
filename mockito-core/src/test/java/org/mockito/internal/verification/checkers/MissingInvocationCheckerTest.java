/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.internal.invocation.InterceptedInvocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class MissingInvocationCheckerTest extends TestBase {

    private InvocationMatcher wanted;
    private List<Invocation> invocations;

    @Mock private IMethods mock;

    @Test
    public void shouldPassBecauseActualInvocationFound() {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(buildSimpleMethod().toInvocation());

        MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
    }

    @Test
    public void shouldReportWantedButNotInvoked() {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(buildDifferentMethod().toInvocation());

        assertThatThrownBy(
                        () -> {
                            MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
                        })
                .isInstanceOf(WantedButNotInvoked.class)
                .hasMessageContainingAll(
                        "Wanted but not invoked:",
                        "mock.simpleMethod()",
                        "However, there was exactly 1 interaction with this mock:",
                        "mock.differentMethod();");
    }

    @Test
    public void shouldReportWantedInvocationDiffersFromActual() {
        wanted = buildIntArgMethod(new InvocationBuilder()).arg(2222).toInvocationMatcher();
        invocations = asList(buildIntArgMethod(new InvocationBuilder()).arg(1111).toInvocation());

        assertThatThrownBy(
                        () -> {
                            MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
                        })
                .isInstanceOf(ArgumentsAreDifferent.class)
                .hasMessageContainingAll(
                        "Argument(s) are different! Wanted:",
                        "mock.intArgumentMethod(2222);",
                        "Actual invocations have different arguments:",
                        "mock.intArgumentMethod(1111);");
    }

    @Test
    public void shouldReportUsingInvocationDescription() {
        wanted = buildIntArgMethod(new CustomInvocationBuilder()).arg(2222).toInvocationMatcher();
        invocations =
                singletonList(
                        buildIntArgMethod(new CustomInvocationBuilder()).arg(1111).toInvocation());

        assertThatThrownBy(
                        () -> {
                            MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
                        })
                .isInstanceOf(ArgumentsAreDifferent.class)
                .hasMessageContainingAll(
                        "Argument(s) are different! Wanted:",
                        "mock.intArgumentMethod(MyCoolPrint(2222));",
                        "Actual invocations have different arguments:",
                        "mock.intArgumentMethod(MyCoolPrint(1111));");
    }

    @Test
    public void shouldSpecifyPosition0WhenWantedInvocationDiffersFromActual() {
        wanted = buildMultiArgsMethod().args("arg1", 2222).toInvocationMatcher();
        invocations = singletonList(buildMultiArgsMethod().args("differs", 2222).toInvocation());

        assertThatThrownBy(
                        () -> {
                            MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
                        })
                .isInstanceOf(ArgumentsAreDifferent.class)
                .hasMessageContainingAll(
                        "Argument(s) are different! Wanted:",
                        "mock.simpleMethod(\"arg1\", 2222);",
                        "Actual invocations have different arguments at position [0]:",
                        "mock.simpleMethod(\"differs\", 2222);");
    }

    @Test
    public void shouldSpecifyPosition1WhenWantedInvocationDiffersFromActual() {
        wanted = buildMultiArgsMethod().args("arg1", 2222).toInvocationMatcher();
        invocations = singletonList(buildMultiArgsMethod().args("arg1", 1111).toInvocation());

        assertThatThrownBy(
                        () -> {
                            MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
                        })
                .isInstanceOf(ArgumentsAreDifferent.class)
                .hasMessageContainingAll(
                        "Argument(s) are different! Wanted:",
                        "mock.simpleMethod(\"arg1\", 2222);",
                        "Actual invocations have different arguments at position [1]:",
                        "mock.simpleMethod(\"arg1\", 1111);");
    }

    @Test
    public void shouldSpecifyPosition0And1WhenWantedInvocationDiffersFromActual() {
        wanted = buildMultiArgsMethod().args("arg1", 2222).toInvocationMatcher();
        invocations = singletonList(buildMultiArgsMethod().args("differs", 1111).toInvocation());

        assertThatThrownBy(
                        () -> {
                            MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
                        })
                .isInstanceOf(ArgumentsAreDifferent.class)
                .hasMessageContainingAll(
                        "Argument(s) are different! Wanted:",
                        "mock.simpleMethod(\"arg1\", 2222);",
                        "Actual invocations have different arguments at positions [0, 1]:",
                        "mock.simpleMethod(\"differs\", 1111);");
    }

    @Test
    public void shouldNotSpecifyPositionWhenWantedSingleArgInvocationSiffersFromActual() {
        wanted = buildIntArgMethod(new CustomInvocationBuilder()).arg(2222).toInvocationMatcher();
        invocations =
                singletonList(
                        buildIntArgMethod(new CustomInvocationBuilder()).arg(1111).toInvocation());

        assertThatThrownBy(
                        () -> {
                            MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
                        })
                .isInstanceOf(ArgumentsAreDifferent.class)
                .hasMessageContainingAll(
                        "Argument(s) are different! Wanted:",
                        "mock.intArgumentMethod(MyCoolPrint(2222));",
                        "Actual invocations have different arguments:",
                        "mock.intArgumentMethod(MyCoolPrint(1111));");
    }

    private InvocationBuilder buildIntArgMethod(InvocationBuilder invocationBuilder) {
        return invocationBuilder.mock(mock).method("intArgumentMethod").argTypes(int.class);
    }

    private InvocationBuilder buildSimpleMethod() {
        return new InvocationBuilder().mock(mock).simpleMethod();
    }

    private InvocationBuilder buildDifferentMethod() {
        return new InvocationBuilder().mock(mock).differentMethod();
    }

    private InvocationBuilder buildMultiArgsMethod() {
        return new InvocationBuilder()
                .mock(mock)
                .method("simpleMethod")
                .argTypes(String.class, Integer.class);
    }

    static class CustomInvocationBuilder extends InvocationBuilder {
        @Override
        protected Invocation createInvocation(
                MockReference<Object> mockRef,
                MockitoMethod mockitoMethod,
                final Object[] arguments,
                RealMethod realMethod,
                Location location,
                int sequenceNumber) {
            return new InterceptedInvocation(
                    mockRef, mockitoMethod, arguments, realMethod, location, sequenceNumber) {
                @Override
                public List<ArgumentMatcher> getArgumentsAsMatchers() {
                    List<ArgumentMatcher> matchers = new ArrayList<>();
                    for (final Object argument : getRawArguments()) {
                        matchers.add(
                                new ArgumentMatcher() {
                                    @Override
                                    public boolean matches(Object a) {
                                        return a == argument;
                                    }

                                    @Override
                                    public String toString() {
                                        return "MyCoolPrint(" + argument + ")";
                                    }
                                });
                    }
                    return matchers;
                }
            };
        }
    }
}
