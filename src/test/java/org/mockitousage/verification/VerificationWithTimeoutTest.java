/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.Stopwatch;
import org.mockitoutil.async.AsyncTesting;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.junit.MockitoJUnit.rule;
import static org.mockitoutil.Stopwatch.createNotStarted;

public class VerificationWithTimeoutTest {

    @Rule public MockitoRule mockito = rule();

    private Stopwatch watch = createNotStarted();

    @Mock private IMethods mock;

    private AsyncTesting async;

    @Before
    public void setUp() {
        async = new AsyncTesting();
    }

    @After
    public void tearDown() {
        async.cleanUp();
    }

    @Test
    public void should_verify_with_timeout() {
        // when
        async.runAfter(50, callMock('c'));
        async.runAfter(500, callMock('c'));

        // then
        verify(mock, timeout(200).only()).oneArg('c');
        verify(mock).oneArg('c'); //sanity check
    }

    @Test
    public void should_verify_with_timeout_and_fail() {
        // when
        async.runAfter(200, callMock('c'));

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, timeout(50).only()).oneArg('c');
            }
        }).isInstanceOf(AssertionError.class).hasMessageContaining("Wanted but not invoked");
        //TODO let's have a specific exception vs. generic assertion error + message
    }

    @Test
    @Ignore //TODO nice to have
    public void should_verify_with_timeout_and_fail_early() {
        // when
        callMock('c');
        callMock('c');

        watch.start();

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, timeout(2000)).oneArg('c');
            }
        }).isInstanceOf(AssertionError.class).hasMessageContaining("Wanted but not invoked");

        watch.assertElapsedTimeIsLessThan(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void should_verify_with_times_x() {
        // when
        async.runAfter(50, callMock('c'));
        async.runAfter(100, callMock('c'));
        async.runAfter(600, callMock('c'));

        // then
        verify(mock, timeout(300).times(2)).oneArg('c');
    }

    @Test
    public void should_verify_with_times_x_and_fail() {
        // when
        async.runAfter(10, callMock('c'));
        async.runAfter(200, callMock('c'));

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, timeout(100).times(2)).oneArg('c');
            }
        }).isInstanceOf(TooLittleActualInvocations.class);
    }

    @Test
    public void should_verify_with_at_least() {
        // when
        async.runAfter(10, callMock('c'));
        async.runAfter(50, callMock('c'));

        // then
        verify(mock, timeout(200).atLeast(2)).oneArg('c');
    }

    @Test
    public void should_verify_with_at_least_once() {
        // when
        async.runAfter(10, callMock('c'));
        async.runAfter(50, callMock('c'));

        // then
        verify(mock, timeout(200).atLeastOnce()).oneArg('c');
    }

    @Test
    public void should_verify_with_at_least_and_fail() {
        // when
        async.runAfter(10, callMock('c'));
        async.runAfter(50, callMock('c'));

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                verify(mock, timeout(100).atLeast(3)).oneArg('c');
            }
        }).isInstanceOf(TooLittleActualInvocations.class);
    }

    @Test
    public void should_verify_with_only() {
        // when
        async.runAfter(10, callMock('c'));
        async.runAfter(300, callMock('c'));

        // then
        verify(mock, timeout(100).only()).oneArg('c');
    }

    @Test
    @Ignore("not testable, probably timeout().only() does not make sense")
    public void should_verify_with_only_and_fail() {
        // when
        async.runAfter(10, callMock('c'));
        async.runAfter(50, callMock('c'));

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, after(200).only()).oneArg('c');
            }
        }).isInstanceOf(AssertionError.class);
    }

    @Test
    @Ignore //TODO nice to have
    public void should_verify_with_only_and_fail_early() {
        // when
        callMock('c');
        callMock('c');

        watch.start();

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, timeout(2000).only()).oneArg('c');
            }
        }).isInstanceOf(AssertionError.class).hasMessageContaining("Wanted but not invoked"); //TODO specific exception

        watch.assertElapsedTimeIsLessThan(1000, TimeUnit.MILLISECONDS);
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
