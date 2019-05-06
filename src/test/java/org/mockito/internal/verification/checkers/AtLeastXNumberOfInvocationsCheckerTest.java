/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.exceptions.verification.TooFewActualInvocations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.verification.checkers.AtLeastXNumberOfInvocationsChecker.checkAtLeastNumberOfInvocations;

public class AtLeastXNumberOfInvocationsCheckerTest   {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldMarkActualInvocationsAsVerifiedInOrder() {
        InOrderContext context = new InOrderContextImpl();
        //given
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        //when
        checkAtLeastNumberOfInvocations(asList(invocation, invocationTwo), new InvocationMatcher(invocation), 1, context);

        //then
        assertThat(invocation.isVerified()).isTrue();
    }

    @Test
    public void shouldReportTooFewInvocationsInOrder() {
        InOrderContext context = new InOrderContextImpl();
        //given
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("iMethods.simpleMethod()");
        exception.expectMessage("Wanted *at least* 2 times");
        exception.expectMessage("But was 1 time");

        //when
        checkAtLeastNumberOfInvocations(asList(invocation, invocationTwo), new InvocationMatcher(invocation), 2, context);


    }

    @Test
    public void shouldMarkActualInvocationsAsVerified() {
        //given
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        //when
        checkAtLeastNumberOfInvocations(asList(invocation, invocationTwo), new InvocationMatcher(invocation), 1);

        //then
        assertThat(invocation.isVerified()).isTrue();
    }

    @Test
    public void shouldReportTooFewInvocations() {
        //given
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        exception.expect(TooFewActualInvocations.class);
        exception.expectMessage("iMethods.simpleMethod()");
        exception.expectMessage("Wanted *at least* 2 times");
        exception.expectMessage("But was 1 time");

        //when
        checkAtLeastNumberOfInvocations(asList(invocation, invocationTwo), new InvocationMatcher(invocation), 2);
    }
}
