/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.async.AsyncTesting;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.timeout;
import static org.mockito.junit.MockitoJUnit.rule;

public class VerificationInOrderWithTimeoutTest {

    @Rule public MockitoRule mockito = rule();

    @Mock private IMethods mock1;
    @Mock private IMethods mock2;

    private AsyncTesting async;

    @Before public void setUp() {
        async = new AsyncTesting();
    }

    @After public void tearDown() {
        async.cleanUp();
    }

    @Test
    public void should_not_allow_in_order_with_after() {
        // expect
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                inOrder(mock1).verify(mock1, after(100)).oneArg('a');
            }
        }).isInstanceOf(MockitoException.class).hasMessageContaining("not implemented to work with InOrder");
        //TODO specific exception
    }

    @Test
    public void should_verify_in_order_with_timeout() {
        // when
        async.runAfter(20, callMock(mock1, 'a'));
        async.runAfter(50, callMock(mock1, 'c'));
        async.runAfter(200, callMock(mock2, 'b'));

        // then
        InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock1, timeout(100)).oneArg('a');
        inOrder.verify(mock2, timeout(500)).oneArg('b');
    }

    @Test
    public void should_verify_in_order_with_timeout_and_fail() {
        // when
        async.runAfter(20, callMock(mock1, 'a'));
        async.runAfter(100, callMock(mock2, 'b'));

        // then
        final InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock2, timeout(300)).oneArg('b');
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                inOrder.verify(mock1, timeout(300)).oneArg('a');
            }
        }).isInstanceOf(VerificationInOrderFailure.class)
            .hasMessageContaining("Wanted but not invoked:\nmock1.oneArg('a');")
            .hasMessageContaining("Wanted anywhere AFTER following interaction:\nmock2.oneArg('b');");
    }

    @Test
    public void should_verify_in_order_with_times_x() {
        // when
        async.runAfter(20, callMock(mock1, 'a'));
        async.runAfter(50, callMock(mock1, 'a'));
        async.runAfter(200, callMock(mock2, 'b'));
        async.runAfter(250, callMock(mock2, 'b'));

        // then
        InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock1, timeout(100).times(2)).oneArg('a');
        inOrder.verify(mock2, timeout(500).times(2)).oneArg('b');
    }

    @Test
    public void should_verify_in_order_with_times_x_and_fail() {
        // when
        async.runAfter(20, callMock(mock1, 'a'));
        async.runAfter(50, callMock(mock1, 'a'));
        async.runAfter(200, callMock(mock2, 'b'));
        async.runAfter(250, callMock(mock2, 'b'));

        // then
        final InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock2, timeout(500).times(2)).oneArg('b');

        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                inOrder.verify(mock1, timeout(100).times(2)).oneArg('a');
            }
        }).isInstanceOf(VerificationInOrderFailure.class)
            .hasMessageContaining("Wanted but not invoked:\nmock1.oneArg('a');")
            .hasMessageContaining("Wanted anywhere AFTER following interaction:\nmock2.oneArg('b');");
    }

    @Test
    public void should_not_allow_in_order_with_only() {
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                inOrder(mock1).verify(mock1, timeout(200).only()).oneArg('a');
            }
        }).isInstanceOf(MockitoException.class).hasMessageContaining("not implemented to work with InOrder");
        //TODO specific exception
    }

    @Test
    public void should_verify_in_order_with_at_least_once() {
        // when
        async.runAfter(20, callMock(mock1, 'a'));
        async.runAfter(50, callMock(mock1, 'a'));
        async.runAfter(100, callMock(mock2, 'b'));
        async.runAfter(120, callMock(mock2, 'b'));

        // then
        InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock1, timeout(200).atLeastOnce()).oneArg('a');
        inOrder.verify(mock2, timeout(500).atLeastOnce()).oneArg('b');
    }

    @Test
    public void should_verify_in_order_with_at_least_once_and_fail() {
        // when
        async.runAfter(20, callMock(mock1, 'a'));
        async.runAfter(50, callMock(mock1, 'a'));
        async.runAfter(100, callMock(mock2, 'b'));
        async.runAfter(120, callMock(mock2, 'b'));

        // then
        final InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock2, timeout(300).atLeastOnce()).oneArg('b');
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                inOrder.verify(mock1, timeout(500).atLeastOnce()).oneArg('a');
            }
        }).isInstanceOf(VerificationInOrderFailure.class)
            .hasMessageContaining("Wanted but not invoked:\nmock1.oneArg('a');")
            .hasMessageContaining("Wanted anywhere AFTER following interaction:\nmock2.oneArg('b');");
    }

    @Test
    public void should_verify_in_order_with_at_least_x() {
        // when
        async.runAfter(20, callMock(mock1, 'a'));
        async.runAfter(50, callMock(mock1, 'a'));
        async.runAfter(100, callMock(mock2, 'b'));
        async.runAfter(120, callMock(mock2, 'b'));

        // then
        InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock1, timeout(200).atLeast(2)).oneArg('a');
        inOrder.verify(mock2, timeout(500).atLeast(2)).oneArg('b');
    }

    @Test
    public void should_verify_in_order_with_at_least_x_and_fail() {
        // when
        async.runAfter(20, callMock(mock1, 'a'));
        async.runAfter(50, callMock(mock1, 'a'));
        async.runAfter(100, callMock(mock2, 'b'));
        async.runAfter(120, callMock(mock2, 'b'));

        // then
        final InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock2, timeout(300).atLeast(2)).oneArg('b');
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                inOrder.verify(mock1, timeout(500).atLeast(2)).oneArg('a');
            }
        }).isInstanceOf(AssertionError.class)
            .hasMessageContaining("Verification in order failure");
    }

    private Runnable callMock(final IMethods mock, final char c) {
        return new Runnable() {
            @Override
            public void run() {
                mock.oneArg(c);
            }
        };
    }
}
