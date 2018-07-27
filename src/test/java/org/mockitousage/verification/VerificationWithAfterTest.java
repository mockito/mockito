/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.assertj.core.api.ThrowableAssert;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.MoreThanAllowedActualInvocations;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.Stopwatch;
import org.mockitoutil.async.AsyncTesting;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.junit.MockitoJUnit.rule;
import static org.mockitoutil.Stopwatch.createNotStarted;

public class VerificationWithAfterTest {

    @Rule public MockitoRule mockito = rule();

    @Mock private IMethods mock;

    private Runnable callMock = new Runnable() {
        public void run() {
            mock.oneArg('1');
        }
    };

    private AsyncTesting async = new AsyncTesting();
    private Stopwatch watch = createNotStarted();

    @After public void tearDown() {
        async.cleanUp();
    }

    /**

     TODO review coverage once again, it still misses some.

     -nothing + fail
     -times(2) + fail
     -atLeastOnce, atLeast(2), fail
     -atMost, fail
     -never, fail
     -only, fail

     */

    @Test
    public void should_verify_normally_with_specific_times() {
        // given
        async.runAfter(100, callMock);

        // then
        verify(mock, after(1000).times(1)).oneArg('1');
    }

    @Test
    public void should_verify_normally_with_AtLeast() {
        // when
        async.runAfter(100, callMock);

        // then
        verify(mock, after(200).atLeast(1)).oneArg('1');
    }

    @Test
    public void should_fail_verification_with_wrong_times() {
        // when
        async.runAfter(100, callMock);

        // then
        verify(mock, times(0)).oneArg('1');

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                verify(mock, after(200).times(2)).oneArg('1');
            }
        }).isInstanceOf(TooLittleActualInvocations.class);
    }

    @Test
    public void should_wait_the_full_time_if_the_test_could_pass() {
        // when
        callMock.run();

        // then
        watch.start();

        try {
            verify(mock, after(200).atLeast(2)).oneArg('1');
            fail("Expected behavior was to throw an exception, and never reach this line");
        } catch (MockitoAssertionError ignored) {
        }

        //we are using '150' instead of '200' below to avoid timing issues (while keeping good coverage)
        watch.assertElapsedTimeIsMoreThan(150, MILLISECONDS);
    }

    @Test
    public void should_fail_early_when_never_is_used() {
        watch.start();

        // when
        async.runAfter(30, callMock);

        // then
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                verify(mock, after(10000).never()).oneArg('1');
            }
        }).isInstanceOf(MoreThanAllowedActualInvocations.class);

        // using generous number to avoid timing issues
        watch.assertElapsedTimeIsLessThan(2000, MILLISECONDS);
    }

    @Test
    public void should_fail_early_when_at_most_is_used() {
        watch.start();

        // when
        async.runAfter(50, callMock);
        async.runAfter(100, callMock);

        // then
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                verify(mock, after(10000).atMost(1)).oneArg('1');
            }
        }).isInstanceOf(MoreThanAllowedActualInvocations.class);

        // using generous number to avoid timing issues
        watch.assertElapsedTimeIsLessThan(2000, MILLISECONDS);
    }
}
