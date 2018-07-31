/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.MoreThanAllowedActualInvocations;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.Stopwatch;
import org.mockitoutil.async.AsyncTesting;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.after;
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

    @Test
    public void should_verify_with_after() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(1000, callMock);

        // then
        verify(mock, after(300)).oneArg('1');
    }

    @Test
    public void should_verify_with_after_and_fail() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(40, callMock);

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, after(600)).oneArg('1');
            }
        }).isInstanceOf(TooManyActualInvocations.class);
    }

    @Test
    public void should_verify_with_time_x() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(50, callMock);
        async.runAfter(600, callMock);

        // then
        verify(mock, after(300).times(2)).oneArg('1');
    }

    @Test
    public void should_verify_with_time_x_and_fail() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(40, callMock);
        async.runAfter(80, callMock);

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, after(300).times(2)).oneArg('1');
            }
        }).isInstanceOf(TooManyActualInvocations.class);
    }

    @Test
    public void should_verify_with_at_least() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(50, callMock);

        // then
        verify(mock, after(300).atLeastOnce()).oneArg('1');
    }

    @Test
    public void should_verify_with_at_least_and_fail() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(50, callMock);
        async.runAfter(600, callMock);

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, after(300).atLeast(3)).oneArg('1');
            }
        }).isInstanceOf(AssertionError.class).hasMessageContaining("Wanted *at least* 3 times"); //TODO specific exception
    }

    @Test
    public void should_verify_with_at_most() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(50, callMock);
        async.runAfter(600, callMock);

        // then
        verify(mock, after(300).atMost(2)).oneArg('1');
    }

    @Test
    public void should_verify_with_at_most_and_fail() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(50, callMock);
        async.runAfter(600, callMock);

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, after(300).atMost(1)).oneArg('1');
            }
        }).isInstanceOf(AssertionError.class).hasMessageContaining("Wanted at most 1 time but was 2"); //TODO specific exception
    }

    @Test
    public void should_verify_with_never() {
        // given
        async.runAfter(500, callMock);

        // then
        verify(mock, after(50).never()).oneArg('1');
    }

    @Test
    public void should_verify_with_never_and_fail() {
        // given
        async.runAfter(10, callMock);

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, after(300).never()).oneArg('1');
            }
        }).isInstanceOf(MoreThanAllowedActualInvocations.class).hasMessageContaining("Wanted at most 0 times but was 1");
    }

    @Test
    public void should_verify_with_only() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(600, callMock);

        // then
        verify(mock, after(300).only()).oneArg('1');
    }

    @Test
    public void should_verify_with_only_and_fail() {
        // given
        async.runAfter(10, callMock);
        async.runAfter(50, callMock);

        // then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                verify(mock, after(300).only()).oneArg('1');
            }
        }).isInstanceOf(AssertionError.class).hasMessageContaining("No interactions wanted here"); //TODO specific exception
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

    @Test
    public void should_fail_early_when_never_is_used() {
        watch.start();

        // when
        async.runAfter(50, callMock);

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
    @Ignore //TODO nice to have
    public void should_fail_early_when_only_is_used() {
        watch.start();

        // when
        async.runAfter(50, callMock);
        async.runAfter(100, callMock);

        // then
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                verify(mock, after(10000).only()).oneArg('1');
            }
        }).isInstanceOf(NoInteractionsWanted.class);

        // using generous number to avoid timing issues
        watch.assertElapsedTimeIsLessThan(2000, MILLISECONDS);
    }

    @Test
    @Ignore //TODO nice to have
    public void should_fail_early_when_time_x_is_used() {
        watch.start();

        // when
        async.runAfter(50, callMock);
        async.runAfter(100, callMock);

        // then
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                verify(mock, after(10000).times(1)).oneArg('1');
            }
        }).isInstanceOf(NoInteractionsWanted.class);

        // using generous number to avoid timing issues
        watch.assertElapsedTimeIsLessThan(2000, MILLISECONDS);
    }
}
