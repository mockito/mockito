/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Rule public ExpectedException exception = ExpectedException.none();

    @Rule public TestName testName = new TestName();

    @Test
    public void shouldReportTooFewActual() throws Exception {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations =
                asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        exception.expect(TooFewActualInvocations.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 2 times");

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 100);
    }

    @Test
    public void shouldReportAllInvocationsStackTrace() throws Exception {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations =
                asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        exception.expect(TooFewActualInvocations.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 2 times");
        exception.expectMessage(containsTimes("-> at", 3));

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 100);
    }

    @Test
    public void shouldNotReportWithLastInvocationStackTraceIfNoInvocationsFound() throws Exception {
        invocations = emptyList();
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expect(TooFewActualInvocations.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 0 times");
        exception.expectMessage(containsTimes("-> at", 1));

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 100);
    }

    @Test
    public void shouldReportWithAllInvocationsStackTrace() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();
        Invocation third = buildSimpleMethod().toInvocation();

        invocations = asList(first, second, third);
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expect(TooManyActualInvocations.class);
        exception.expectMessage("" + first.getLocation());
        exception.expectMessage("" + second.getLocation());
        exception.expectMessage("" + third.getLocation());
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 2);
    }

    @Test
    public void shouldReportTooManyActual() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();

        invocations = asList(first, second);
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expectMessage("Wanted 1 time");
        exception.expectMessage("But was 2 times");

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 1);
    }

    @Test
    public void shouldReportNeverWantedButInvokedWithArgs() throws Exception {
        Invocation invocation = buildSimpleMethodWithArgs("arg1").toInvocation();

        invocations = Collections.singletonList(invocation);
        wanted = buildSimpleMethodWithArgs("arg1").toInvocationMatcher();

        exception.expect(NeverWantedButInvoked.class);
        exception.expectMessage("Never wanted here");
        exception.expectMessage("But invoked here");
        exception.expectMessage("" + invocation.getLocation() + " with arguments: [arg1]");

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 0);
    }

    @Test
    public void shouldReportNeverWantedButInvokedWithArgs_multipleInvocations() throws Exception {
        Invocation first = buildSimpleMethodWithArgs("arg1").toInvocation();
        Invocation second = buildSimpleMethodWithArgs("arg1").toInvocation();

        invocations = asList(first, second);
        wanted = buildSimpleMethodWithArgs("arg1").toInvocationMatcher();

        exception.expect(NeverWantedButInvoked.class);
        exception.expectMessage("Never wanted here");
        exception.expectMessage("But invoked here");
        exception.expectMessage("" + first.getLocation() + " with arguments: [arg1]");
        exception.expectMessage("" + second.getLocation() + " with arguments: [arg1]");

        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, 0);
    }

    @Test
    public void shouldMarkInvocationsAsVerified() throws Exception {
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

    private InvocationBuilder buildDifferentMethodWithArgs(String arg) {
        return new InvocationBuilder().mock(mock).differentMethod().args(arg);
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

        public void describeTo(Description description) {
            description.appendText("containing '" + expected + "' exactly " + amount + " times");
        }
    }
}
