/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.junit.MockitoJUnit.rule;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

public class VerificationWithTimeoutTest {

    @Rule
    public MockitoRule mockito = rule();

    @Rule
    public ExpectedException exception = none();

    @Mock
    private IMethods mock;

    private ScheduledExecutorService executor;

    @Before
    public void setUp() {
        executor = newSingleThreadScheduledExecutor();
    }

    @After
    public void tearDown() throws InterruptedException {
        executor.shutdownNow();
        executor.awaitTermination(5, SECONDS);
    }

    @Test
    public void shouldVerifyWithTimeout() throws Exception {
        // given
        callAsyncWithDelay(mock, 'c', 20, MILLISECONDS);

        // then
        verify(mock, timeout(100)).oneArg('c');

        verify(mock, timeout(100).atLeastOnce()).oneArg('c');
        verify(mock, timeout(100).times(1)).oneArg('c');

        verify(mock).oneArg('c');
        verify(mock, times(1)).oneArg('c');
    }

    @Test
    public void shouldFailVerificationWithTimeout() throws Exception {
        // given
        callAsyncWithDelay(mock, 'c', 30, MILLISECONDS);

        // then
        verify(mock, never()).oneArg('c');

        exception.expect(MockitoAssertionError.class);
        verify(mock, timeout(20).atLeastOnce()).oneArg('c');
    }

    @Test
    public void shouldAllowMixingOtherModesWithTimeout() throws Exception {
        // given
        callAsyncWithDelay(mock, 'c', 20, MILLISECONDS);
        callAsyncWithDelay(mock, 'c', 20, MILLISECONDS);

        // then
        verify(mock, timeout(100).atLeast(1)).oneArg('c');
        verify(mock, timeout(100).times(2)).oneArg('c');
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldAllowMixingOtherModesWithTimeoutAndFail() throws Exception {
        // given
        callAsyncWithDelay(mock, 'c', 20, MILLISECONDS);
        callAsyncWithDelay(mock, 'c', 20, MILLISECONDS);

        // then
        verify(mock, timeout(40).atLeast(1)).oneArg('c');
        exception.expect(TooLittleActualInvocations.class);
        verify(mock, timeout(100).times(3)).oneArg('c');
    }

    @Test
    public void shouldAllowMixingOnlyWithTimeout() throws Exception {
        // given
        callAsyncWithDelay(mock, 'c', 10, MILLISECONDS);

        // then
        verify(mock, never()).oneArg('c');
        verify(mock, timeout(30).only()).oneArg('c');
    }

    @Test
    public void shouldAllowMixingOnlyWithTimeoutAndFail() throws Exception {
        // given
        callAsyncWithDelay(mock, 'c', 20, MILLISECONDS);

        // when
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
    public void canIgnoreInvocationsWithJunit() {
        // when
        callAsyncWithDelay(mock, '0', 0, MILLISECONDS);
        callAsyncWithDelay(mock, '1', 0, MILLISECONDS);
        callAsyncWithDelay(mock, '2', 20, MILLISECONDS);

        // then
        verify(mock, timeout(50)).oneArg('1');
        verify(mock, timeout(50)).oneArg('2');
    }

    @Test
    public void shouldAllowTimeoutVerificationInOrder() throws Exception {
        callAsyncWithDelay(mock, '1', 30, MILLISECONDS);

        mock.oneArg('x');

        // then
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).oneArg('x');
        inOrder.verify(mock, never()).oneArg('1');
        inOrder.verify(mock, timeout(100)).oneArg('1');
    }

    private void callAsyncWithDelay(final IMethods mock, final char value, long delay, TimeUnit unit) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                mock.oneArg(value);
            }
        };
        executor.schedule(task, delay, unit);
    }
}