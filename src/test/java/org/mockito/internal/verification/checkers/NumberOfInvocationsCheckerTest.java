/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.TooFewActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockitousage.IMethods;

@RunWith(MockitoJUnitRunner.class)
public class NumberOfInvocationsCheckerTest {

    private InvocationMatcher wanted;

    private List<Invocation> invocations;

    @Mock private IMethods mock;

    @Rule public TestName testName = new TestName();

    @Test
    public void shouldReportTooFewActual() {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations =
                asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 100);
                        })
                .isInstanceOf(TooFewActualInvocations.class)
                .hasMessageContainingAll(
                        "mock.simpleMethod()", "Wanted 100 times", "But was 2 times");
    }

    @Test
    public void shouldReportAllInvocationsStackTrace() {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations =
                asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 100);
                        })
                .isInstanceOf(TooFewActualInvocations.class)
                .hasMessageContainingAll(
                        "mock.simpleMethod()", "Wanted 100 times", "But was 2 times")
                .has(messageContaining("-> at", 3));
    }

    @Test
    public void shouldNotReportWithLastInvocationStackTraceIfNoInvocationsFound() {
        invocations = emptyList();
        wanted = buildSimpleMethod().toInvocationMatcher();

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 100);
                        })
                .isInstanceOf(TooFewActualInvocations.class)
                .hasMessageContainingAll(
                        "mock.simpleMethod()", "Wanted 100 times", "But was 0 times")
                .has(messageContaining("-> at", 1));
    }

    @Test
    public void shouldReportWithAllInvocationsStackTrace() {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();
        Invocation third = buildSimpleMethod().toInvocation();

        invocations = asList(first, second, third);
        wanted = buildSimpleMethod().toInvocationMatcher();

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 2);
                        })
                .isInstanceOf(TooManyActualInvocations.class)
                .hasMessageContainingAll(
                        "" + first.getLocation(),
                        "" + second.getLocation(),
                        "" + third.getLocation());
    }

    @Test
    public void shouldReportTooManyActual() {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();

        invocations = asList(first, second);
        wanted = buildSimpleMethod().toInvocationMatcher();

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 1);
                        })
                .hasMessageContainingAll("Wanted 1 time", "But was 2 times");
    }

    @Test
    public void shouldReportNeverWantedButInvokedWithArgs() {
        Invocation invocation = buildSimpleMethodWithArgs("arg1").toInvocation();

        invocations = Collections.singletonList(invocation);
        wanted = buildSimpleMethodWithArgs("arg1").toInvocationMatcher();

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 0);
                        })
                .isInstanceOf(NeverWantedButInvoked.class)
                .hasMessageContainingAll(
                        "Never wanted here",
                        "But invoked here",
                        "" + invocation.getLocation() + " with arguments: [arg1]");
    }

    @Test
    public void shouldReportNeverWantedButInvokedWithArgs_multipleInvocations() {
        Invocation first = buildSimpleMethodWithArgs("arg1").toInvocation();
        Invocation second = buildSimpleMethodWithArgs("arg1").toInvocation();

        invocations = asList(first, second);
        wanted = buildSimpleMethodWithArgs("arg1").toInvocationMatcher();

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 0);
                        })
                .isInstanceOf(NeverWantedButInvoked.class)
                .hasMessageContainingAll(
                        "Never wanted here",
                        "But invoked here",
                        "" + first.getLocation() + " with arguments: [arg1]",
                        "" + second.getLocation() + " with arguments: [arg1]");
    }

    @Test
    public void shouldMarkInvocationsAsVerified() {
        Invocation invocation = buildSimpleMethod().toInvocation();
        assertThat(invocation.isVerified()).isFalse();

        invocations = asList(invocation);
        wanted = buildSimpleMethod().toInvocationMatcher();
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1);
        assertThat(invocation.isVerified()).isTrue();
    }

    private InvocationBuilder buildSimpleMethod() {
        return new InvocationBuilder().mock(mock).simpleMethod();
    }

    private InvocationBuilder buildSimpleMethodWithArgs(String arg) {
        return new InvocationBuilder().mock(mock).simpleMethod().args(arg);
    }

    private static Condition<? super Throwable> messageContaining(
            String value, int amountOfOccurrences) {
        return new ThrowableMessageContainsOccurrencesCondition(value, amountOfOccurrences);
    }

    private static class ThrowableMessageContainsOccurrencesCondition extends Condition<Throwable> {

        private final String value;
        private final int expectedOccurrences;

        public ThrowableMessageContainsOccurrencesCondition(String value, int expectedOccurrences) {
            this.value = value;
            this.expectedOccurrences = expectedOccurrences;

            as("exactly %s occurrences of \"%s\"", expectedOccurrences, value);
        }

        @Override
        public boolean matches(Throwable ex) {
            int lastIndex = 0;
            int count = 0;
            while (lastIndex != -1) {
                lastIndex = ex.getMessage().indexOf(value, lastIndex);
                if (lastIndex != -1) {
                    count++;
                    lastIndex += value.length();
                }
            }
            return count == expectedOccurrences;
        }
    }
}
