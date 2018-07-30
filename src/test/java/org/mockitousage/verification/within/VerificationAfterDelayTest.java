/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification.within;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.Stopwatch;
import org.mockitoutil.async.AsyncTesting;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.within;
import static org.mockito.junit.MockitoJUnit.rule;
import static org.mockitoutil.Stopwatch.createNotStarted;

public class VerificationAfterDelayTest {

    @Rule
    public MockitoRule mockito = rule();

    @Rule
    public ExpectedException exception = none();

    @Mock
    private IMethods mock;

    @Captor
    private ArgumentCaptor<Character> captor;

    private Stopwatch stopWatch;

    private AsyncTesting async;

    private Runnable callMock = new Runnable() {
        @Override
        public void run() {
            mock.oneArg('1');
        }
    };

    @Before
    public void setUp() {
        async = new AsyncTesting();
        stopWatch = createNotStarted();
    }

    @After
    public void tearDown() {
        async.cleanUp();

    }

    @Test
    public void shouldVerifyNormallyWithSpecificTimes() throws Exception {
        async.runAfter(20, callMock);

        verify(mock, within(100, MILLISECONDS).times(1)).oneArg('1');
    }

    @Test
    public void shouldVerifyNormallyWithAtLeast() throws Exception {
        async.runAfter(20, callMock);

        verify(mock, within(100, MILLISECONDS).atLeast(1)).oneArg('1');
    }

    @Test
    public void shouldFailVerificationWithWrongTimes() throws Exception {
        async.runAfter(20, callMock);

        verify(mock, times(0)).oneArg('1');

        exception.expect(MockitoAssertionError.class);
        verify(mock, within(100, MILLISECONDS).times(2)).oneArg('1');
    }

    @Test
    public void shouldWaitTheFullTimeIfTheTestCouldPass() throws Exception {
        async.runAfter(20, callMock);
        stopWatch.start();

        try {
            verify(mock, within(100, MILLISECONDS).atLeast(2)).oneArg('1');
            fail("Expected behavior was to throw an exception, and never reach this line");
        } catch (MockitoAssertionError ignored) {
        }

        stopWatch.assertElapsedTimeIsMoreThan(100, MILLISECONDS);
    }

    @Test(timeout = 200)
    public void shouldStopEarlyIfTestIsDefinitelyFailed() throws Exception {
        async.runAfter(20, callMock);

        exception.expect(MockitoAssertionError.class);
        verify(mock, within(99, DAYS).never()).oneArg('1');
    }

    /**
     * Test for issue #345.
     */
    @Test
    public void shouldReturnListOfArgumentsWithSameSizeAsGivenInAtMostVerification() {
        int n = 3;

        exerciseMockNTimes(n);

        stopWatch.start();

        verify(mock, within(200, MILLISECONDS).atMost(n)).oneArg((char) captor.capture());

        stopWatch.assertElapsedTimeIsMoreThan(200, MILLISECONDS);
        assertThat(captor.getAllValues()).containsExactly('0', '1', '2');
    }

    @Test
    public void shouldReturnListOfArgumentsWithSameSizeAsGivenInTimesVerification() {
        int n = 3;

        exerciseMockNTimes(n);

        verify(mock, within(200, MILLISECONDS).times(n)).oneArg((char) captor.capture());
        assertEquals(n, captor.getAllValues().size());
        assertEquals('0', (char) captor.getAllValues().get(0));
        assertEquals('1', (char) captor.getAllValues().get(1));
        assertEquals('2', (char) captor.getAllValues().get(2));
    }

    @Test
    public void shouldReturnListOfArgumentsWithSameSizeAsGivenInAtLeastVerification() {
        // given
        int n = 3;

        // when
        exerciseMockNTimes(n);

        // Then
        verify(mock, within(200, MILLISECONDS).atLeast(n)).oneArg((char) captor.capture());
        assertEquals(n, captor.getAllValues().size());
        assertEquals('0', (char) captor.getAllValues().get(0));
        assertEquals('1', (char) captor.getAllValues().get(1));
        assertEquals('2', (char) captor.getAllValues().get(2));
    }

    private void exerciseMockNTimes(int n) {
        for (int i = 0; i < n; i++) {
            mock.oneArg((char) ('0' + i));
        }
    }

}
