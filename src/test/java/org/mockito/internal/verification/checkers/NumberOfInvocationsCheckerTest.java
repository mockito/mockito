/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class NumberOfInvocationsCheckerTest {

    private NumberOfInvocationsChecker checker;

    private InvocationMatcher wanted;

    private List<Invocation> invocations;

    @Mock
    private IMethods mock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setup() {
        checker = new NumberOfInvocationsChecker();

    }

    @Test
    public void shouldReportTooLittleActual() throws Exception {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        exception.expect(TooLittleActualInvocations.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 2 times");

        checker.check(invocations, wanted, 100);
    }

    @Test
    public void shouldReportWithLastInvocationStackTrace() throws Exception {
        wanted = buildSimpleMethod().toInvocationMatcher();
        invocations = asList(buildSimpleMethod().toInvocation(), buildSimpleMethod().toInvocation());

        exception.expect(TooLittleActualInvocations.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 2 times");
        exception.expectMessage(containsTimes("-> at", 2));

        checker.check(invocations, wanted, 100);
    }

    @Test
    public void shouldNotReportWithLastInvocationStackTraceIfNoInvocationsFound() throws Exception {
        invocations = emptyList();
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expect(TooLittleActualInvocations.class);
        exception.expectMessage("mock.simpleMethod()");
        exception.expectMessage("Wanted 100 times");
        exception.expectMessage("But was 0 times");
        exception.expectMessage(containsTimes("-> at", 1));

        checker.check(invocations, wanted, 100);
    }

    @Test
    public void shouldReportWithFirstUndesiredInvocationStackTrace() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();
        Invocation third = buildSimpleMethod().toInvocation();

        invocations = asList(first, second, third);
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expect(TooManyActualInvocations.class);
        exception.expectMessage("" + third.getLocation());
        checker.check(invocations, wanted, 2);
    }

    @Test
    public void shouldReportTooManyActual() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();
        Invocation second = buildSimpleMethod().toInvocation();

        invocations = asList(first, second);
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expectMessage("Wanted 1 time");
        exception.expectMessage("But was 2 times");

        checker.check(invocations, wanted, 1);
    }

    @Test
    public void shouldReportNeverWantedButInvoked() throws Exception {
        Invocation first = buildSimpleMethod().toInvocation();

        invocations = asList(first);
        wanted = buildSimpleMethod().toInvocationMatcher();

        exception.expect(NeverWantedButInvoked.class);
        exception.expectMessage("Never wanted here");
        exception.expectMessage("But invoked here");
        exception.expectMessage("" + first.getLocation());

        checker.check(invocations, wanted, 0);
    }

	@Test
	public void shouldMarkInvocationsAsVerified() throws Exception {
		Invocation invocation = buildSimpleMethod().toInvocation();
		assertThat(invocation.isVerified()).isFalse();

		invocations = asList(invocation);
		wanted = buildSimpleMethod().toInvocationMatcher();
		checker.check(invocations, wanted, 1);
		assertThat(invocation.isVerified()).isTrue();
	}

    private InvocationBuilder buildSimpleMethod() {
        return new InvocationBuilder().mock(mock).simpleMethod();
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