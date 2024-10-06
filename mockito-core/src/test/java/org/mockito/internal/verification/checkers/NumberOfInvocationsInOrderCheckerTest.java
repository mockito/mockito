/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;

public class NumberOfInvocationsInOrderCheckerTest {

    private InvocationMatcher wanted;
    private List<Invocation> invocations;
    private InOrderContext context;

    private IMethods mock;

    @Before
    public void setup() {
        context = new InOrderContextImpl();
        mock = mock(IMethods.class, "mock");
    }

    @Test
    public void shouldPassIfWantedIsZeroAndMatchingChunkIsEmpty() {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = emptyList();

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 0, context);
    }

    @Test
    public void shouldPassIfChunkMatches() {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(buildSimpleMethod().toInvocation());
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1, context);
    }

    @Test
    public void shouldReportTooFewInvocations() {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();

        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(first, second);

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 4, context);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "mock.simpleMethod()", "Wanted 4 times", "But was 2 times");
    }

    @Test
    public void shouldMarkAsVerifiedInOrder() {
        Invocation invocation = buildSimpleMethod().toInvocation();

        invocations = asList(invocation);
        wanted = buildSimpleMethod().toInvocationMatcher();

        assertThat(context.isVerified(invocation)).isFalse();
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1, context);
        assertThat(context.isVerified(invocation)).isTrue();
    }

    @Test
    public void shouldReportTooFewActual() {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations =
                asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 100, context);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "mock.simpleMethod()", "Wanted 100 times", "But was 2 times");
    }

    @Test
    public void shouldReportWithAllInvocationsStackTrace() {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations =
                asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 100, context);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
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
                                    invocations, wanted, 100, context);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "mock.simpleMethod()", "Wanted 100 times", "But was 0 times")
                .has(messageContaining("-> at", 1));
    }

    @Test
    public void shouldReportWithFirstUndesiredInvocationStackTrace() {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();
        Invocation third = buildSimpleMethod().toInvocation();

        invocations = asList(first, second, third);
        wanted = buildSimpleMethod().toInvocationMatcher();

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 2, context);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContaining("" + third.getLocation());
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
                                    invocations, wanted, 1, context);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll("Wanted 1 time", "But was 2 times");
    }

    @Test
    public void shouldReportNeverWantedButInvoked() {
        Invocation first = buildSimpleMethod().toInvocation();

        invocations = asList(first);
        wanted = buildSimpleMethod().toInvocationMatcher();

        assertThatThrownBy(
                        () -> {
                            NumberOfInvocationsChecker.checkNumberOfInvocations(
                                    invocations, wanted, 0, context);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "mock.simpleMethod()",
                        "Wanted 0 times",
                        "But was 1 time:",
                        "" + first.getLocation());
    }

    @Test
    public void shouldMarkInvocationsAsVerified() {
        Invocation invocation = buildSimpleMethod().toInvocation();
        assertThat(invocation.isVerified()).isFalse();

        invocations = asList(invocation);
        wanted = buildSimpleMethod().toInvocationMatcher();
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1, context);
        assertThat(invocation.isVerified()).isTrue();
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

    private InvocationBuilder buildSimpleMethod() {
        return new InvocationBuilder().mock(mock).simpleMethod();
    }
}
