/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.RetryRule;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.junit.MockitoJUnit.rule;

public class VerificationWithTimeoutTest {

    @Rule
    public MockitoRule mockito = rule();

    @Rule
    public RetryRule retryRule = RetryRule.attempts(4);

    @Rule
    public ExpectedException exception = none();

    @Mock
    private IMethods mock;

    private DelayedExecution delayedExecution;

    @Before
    public void setUp() {
        delayedExecution = new DelayedExecution();
    }

    @After
    public void tearDown() throws InterruptedException {
        delayedExecution.close();
    }

    @Test
    public void shouldVerifyWithTimeout() throws Exception {
        // when
        delayedExecution.callAsync(30, MILLISECONDS, callMock('c'));

        // then
        verify(mock, timeout(100)).oneArg('c');
        verify(mock, timeout(100).atLeastOnce()).oneArg('c');
        verify(mock, timeout(100).times(1)).oneArg('c');
        verify(mock).oneArg('c');
        verify(mock, times(1)).oneArg('c');
    }

    @Test
    public void shouldFailVerificationWithTimeout() throws Exception {
        // when
        delayedExecution.callAsync(30, MILLISECONDS, callMock('c'));

        // then
        verify(mock, never()).oneArg('c');
        exception.expect(MockitoAssertionError.class);
        verify(mock, timeout(20).atLeastOnce()).oneArg('c');
    }

    @Test
    public void shouldAllowMixingOtherModesWithTimeout() throws Exception {
        // when
        delayedExecution.callAsync(10, MILLISECONDS, callMock('c'));
        delayedExecution.callAsync(10, MILLISECONDS, callMock('c'));

        // then
        verify(mock, timeout(100).atLeast(1)).oneArg('c');
        verify(mock, timeout(100).times(2)).oneArg('c');
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldAllowMixingOtherModesWithTimeoutAndFail() throws Exception {
        // when
        delayedExecution.callAsync(10, MILLISECONDS, callMock('c'));
        delayedExecution.callAsync(10, MILLISECONDS, callMock('c'));

        // then
        verify(mock, timeout(100).atLeast(1)).oneArg('c');
        exception.expect(TooLittleActualInvocations.class);
        verify(mock, timeout(100).times(3)).oneArg('c');
    }

    @Test
    public void shouldAllowMixingOnlyWithTimeout() throws Exception {
        // when
        delayedExecution.callAsync(30, MILLISECONDS, callMock('c'));

        // then
        verify(mock, never()).oneArg('c');
        verify(mock, timeout(100).only()).oneArg('c');
    }

    @Test
    public void shouldAllowMixingOnlyWithTimeoutAndFail() throws Exception {
        // when
        delayedExecution.callAsync(30, MILLISECONDS, callMock('c'));

        // and when
        mock.oneArg('x');

        // then
        verify(mock, never()).oneArg('c');
        exception.expect(NoInteractionsWanted.class);
        verify(mock, timeout(100).only()).oneArg('c');
    }

    /**
     * This test is JUnit-specific because the code behaves different if JUnit
     * is used.
     */
    @Test
    public void canIgnoreInvocationsWithJunit() throws InterruptedException {
        // when
        delayedExecution.callAsync(10, MILLISECONDS, callMock('1'));

        // then
        verify(mock, timeout(50)).oneArg('1');

        // when
        delayedExecution.callAsync(10, MILLISECONDS, callMock('2'));
        delayedExecution.callAsync(20, MILLISECONDS, callMock('3'));

        // then
        verify(mock, timeout(50)).oneArg('3');
    }

    @Test
    public void shouldAllowTimeoutVerificationInOrder() throws Exception {
        // when
        delayedExecution.callAsync(50, MILLISECONDS, callMock('1'));

        // and when
        mock.oneArg('x');

        // then
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).oneArg('x');
        inOrder.verify(mock, never()).oneArg('1');
        inOrder.verify(mock, timeout(100)).oneArg('1');
    }

    private Runnable callMock(final char c) {
        return new Runnable() {
            @Override
            public void run() {
                mock.oneArg(c);
            }
        };
    }
}
