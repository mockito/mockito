/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class OnlyTest extends TestBase {

    @Mock IMethods mock;

    Only only = new Only();

    public static class VerificationDataStub implements VerificationData {
        private final Invocation[] invocations;
        private final InvocationMatcher wanted;

        public VerificationDataStub(InvocationMatcher wanted, Invocation... invocations) {
            this.invocations = invocations;
            this.wanted = wanted;
        }

        public List<Invocation> getAllInvocations() {
            return Arrays.asList(invocations);
        }

        @Override
        public MatchableInvocation getTarget() {
            return wanted;
        }

        public InvocationMatcher getWanted() {
            return wanted;
        }
    }

    @Test
    public void shouldMarkAsVerifiedWhenAssertionSucceded() {
        // given
        Invocation invocation = new InvocationBuilder().toInvocation();
        assertFalse(invocation.isVerified());

        // when
        only.verify(new VerificationDataStub(new InvocationMatcher(invocation), invocation));

        // then
        assertTrue(invocation.isVerified());
    }

    @Test
    public void shouldNotMarkAsVerifiedWhenWantedButNotInvoked() {
        // given
        Invocation invocation = new InvocationBuilder().toInvocation();
        assertFalse(invocation.isVerified());

        // when
        try {
            only.verify(
                    new VerificationDataStub(
                            new InvocationBuilder().toInvocationMatcher(), invocation));
            fail();
        } catch (WantedButNotInvoked e) {
        }

        // then
        assertFalse(invocation.isVerified());
    }

    @Test
    public void shouldMarkWantedOnlyAsVerified() {

        // given
        InvocationBuilder invocationBuilder = new InvocationBuilder();
        Invocation wanted = invocationBuilder.mock(mock).simpleMethod().toInvocation();
        Invocation different = new InvocationBuilder().mock(mock).differentMethod().toInvocation();

        // when
        try {
            only.verify(
                    new VerificationDataStub(
                            invocationBuilder.toInvocationMatcher(), wanted, different));
            fail();
        } catch (NoInteractionsWanted e) {
        }

        // then
        assertTrue(wanted.isVerified());
        assertFalse(different.isVerified());
    }

    @Test
    public void shouldMarkMultipleInvocationAsVerified() {

        // given
        InvocationBuilder invocationBuilder = new InvocationBuilder();
        Invocation wanted = invocationBuilder.mock(mock).simpleMethod().toInvocation();
        Invocation different = invocationBuilder.mock(mock).differentMethod().toInvocation();

        // when
        try {
            only.verify(
                    new VerificationDataStub(
                            invocationBuilder.toInvocationMatcher(), wanted, different));
            fail();
        } catch (NoInteractionsWanted e) {
        }

        try {
            only.verify(
                    new VerificationDataStub(
                            invocationBuilder.toInvocationMatcher(), different, wanted));
            fail();
        } catch (WantedButNotInvoked e) {
        }

        // then
        assertTrue(wanted.isVerified());
        assertTrue(different.isVerified());
    }
}
