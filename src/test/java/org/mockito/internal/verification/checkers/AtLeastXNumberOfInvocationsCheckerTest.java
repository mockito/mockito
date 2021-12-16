/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.internal.verification.checkers.AtLeastXNumberOfInvocationsChecker.checkAtLeastNumberOfInvocations;

import org.junit.Test;
import org.mockito.exceptions.verification.TooFewActualInvocations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;

public class AtLeastXNumberOfInvocationsCheckerTest {

    @Test
    public void shouldMarkActualInvocationsAsVerifiedInOrder() {
        InOrderContext context = new InOrderContextImpl();
        // given
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        // when
        checkAtLeastNumberOfInvocations(
                asList(invocation, invocationTwo), new InvocationMatcher(invocation), 1, context);

        // then
        assertThat(invocation.isVerified()).isTrue();
    }

    @Test
    public void shouldReportTooFewInvocationsInOrder() {
        InOrderContext context = new InOrderContextImpl();
        // given
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        // when
        assertThatThrownBy(
                        () ->
                                checkAtLeastNumberOfInvocations(
                                        asList(invocation, invocationTwo),
                                        new InvocationMatcher(invocation),
                                        2,
                                        context))
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "iMethods.simpleMethod();", "Wanted *at least* 2 times", "But was 1 time");
    }

    @Test
    public void shouldMarkActualInvocationsAsVerified() {
        // given
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        // when
        checkAtLeastNumberOfInvocations(
                asList(invocation, invocationTwo), new InvocationMatcher(invocation), 1);

        // then
        assertThat(invocation.isVerified()).isTrue();
    }

    @Test
    public void shouldReportTooFewInvocations() {
        // given
        Invocation invocation = new InvocationBuilder().simpleMethod().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().differentMethod().toInvocation();

        // when
        assertThatThrownBy(
                        () -> {
                            checkAtLeastNumberOfInvocations(
                                    asList(invocation, invocationTwo),
                                    new InvocationMatcher(invocation),
                                    2);
                        })
                .isInstanceOf(TooFewActualInvocations.class)
                .hasMessageContainingAll(
                        "iMethods.simpleMethod();", "Wanted *at least* 2 times", "But was 1 time");
    }
}
