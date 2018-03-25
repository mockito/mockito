/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class NumberOfInvocationsInOrderCheckerTest {

    private InvocationMatcher wanted;
    private List<Invocation> invocations;
    private InOrderContext context;

    private IMethods mock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    public void shouldPassIfChunkMatches() throws Exception {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(buildSimpleMethod().toInvocation());
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1, context);
    }

    @Test
    public void shouldReportTooLittleInvocations() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();

        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(first, second);

        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 4 times");
        exception.expectMessage("But was 2 times");

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 4, context);
    }

    @Test
    public void shouldMarkAsVerifiedInOrder() throws Exception {
        Invocation invocation = buildSimpleMethod().toInvocation();

        invocations = asList(invocation);
        wanted = buildSimpleMethod().toInvocationMatcher();

        assertThat(context.isVerified(invocation)).isFalse();
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1, context);
        assertThat(context.isVerified(invocation)).isTrue();
    }

    @Test
    public void shouldReportTooLittleActual() throws Exception {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 2 times");

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 100, context);
    }

    @Test
    public void shouldReportWithAllInvocationsStackTrace() throws Exception {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 2 times");
        exception.expectMessage(containsTimes("-> at", 3));

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 100, context);

    }

    @Test
    public void shouldNotReportWithLastInvocationStackTraceIfNoInvocationsFound() throws Exception {
        invocations = emptyList();
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 0 times");
        exception.expectMessage(containsTimes("-> at", 1));

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 100, context);
    }

    @Test
    public void shouldReportWithFirstUndesiredInvocationStackTrace() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();
        Invocation third = buildSimpleMethod().toInvocation();

        invocations = asList(first, second, third);
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("" + third.getLocation());
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 2, context);
    }

    @Test
    public void shouldReportTooManyActual() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();

        invocations = asList(first, second);
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expectMessage("Wanted 1 time");
        exception.expectMessage("But was 2 times");

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1, context);
    }

    @Test
    public void shouldReportNeverWantedButInvoked() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();

        invocations = asList(first);
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 0 times");
        exception.expectMessage("But was 1 time:");
        exception.expectMessage("" + first.getLocation());

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 0, context);
    }

    @Test
    public void shouldMarkInvocationsAsVerified() throws Exception {
        Invocation invocation = buildSimpleMethod().toInvocation();
        assertThat(invocation.isVerified()).isFalse();

        invocations = asList(invocation);
        wanted = buildSimpleMethod().toInvocationMatcher();
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1, context);
        assertThat(invocation.isVerified()).isTrue();
    }

    private static BaseMatcher<String> containsTimes(String value, int amount) {
        return new StringContainsNumberMatcher(value, amount);
    }

    private static class StringContainsNumberMatcher extends TypeSafeMatcher<String> {

        private final String expected;

        private final int amount;

        StringContainsNumberMatcher(String expected, int amount) {
            this.expected = expected;
            this.amount = amount;
        }

        @Override
        public boolean matchesSafely(String text) {
            int lastIndex = 0;
            int count = 0;
            while (lastIndex != -1) {
                lastIndex = text.indexOf(expected, lastIndex);
                if (lastIndex != -1) {
                    count++;
                    lastIndex += expected.length();
                }
            }
            return count == amount;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("containing '" + expected + "' exactly " + amount + " times");
        }

    }

    private InvocationBuilder buildSimpleMethod() {
        return new InvocationBuilder().mock(mock).simpleMethod();
    }
}
