/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class VerificationWithTimeoutTest extends TestBase {

    List<Exception> exceptions = new LinkedList<Exception>();

    @After
    public void after() {
        //making sure there are no threading related exceptions
        assertTrue(exceptions.isEmpty());
        exceptions.clear();
    }

    @Mock
    private List<String> mock;

    @Test
    public void shouldVerifyWithTimeout() throws Exception {
        //given
        Thread t = waitAndExerciseMock(20);

        //when
        t.start();

        //then
        verify(mock, timeout(100)).clear();

        verify(mock, timeout(100).atLeastOnce()).clear();
        verify(mock, timeout(100).times(1)).clear();


        verify(mock).clear();
        verify(mock, times(1)).clear();
    }

    @Test
    public void shouldFailVerificationWithTimeout() throws Exception {
        //given
        Thread t = waitAndExerciseMock(80);

        //when
        t.start();

        //then
        verify(mock, never()).clear();
        try {
            verify(mock, timeout(20).atLeastOnce()).clear();
            fail();
        } catch (MockitoAssertionError e) {
        }
    }

    @Test
    public void shouldAllowMixingOtherModesWithTimeout() throws Exception {
        //given
        Thread t1 = waitAndExerciseMock(30);
        Thread t2 = waitAndExerciseMock(30);

        //when
        t1.start();
        t2.start();

        //then
        verify(mock, timeout(50).atLeast(1)).clear();
        verify(mock, timeout(50).times(2)).clear();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldAllowMixingOtherModesWithTimeoutAndFail() throws Exception {
        //given
        Thread t1 = waitAndExerciseMock(30);
        Thread t2 = waitAndExerciseMock(30);

        //when
        t1.start();
        t2.start();

        //then
        verify(mock, timeout(50).atLeast(1)).clear();
        try {
            verify(mock, timeout(100).times(3)).clear();
            fail();
        } catch (TooLittleActualInvocations e) {}
    }

    @Test
    public void shouldAllowMixingOnlyWithTimeout() throws Exception {
        //given
        Thread t1 = waitAndExerciseMock(20);

        //when
        t1.start();

        //then
        verify(mock, never()).clear();
        verify(mock, timeout(40).only()).clear();
    }

    @Test
    public void shouldAllowMixingOnlyWithTimeoutAndFail() throws Exception {
        //given
        Thread t1 = waitAndExerciseMock(20);

        //when
        t1.start();
        mock.add("foo");

        //then
        verify(mock, never()).clear();
        try {
            verify(mock, timeout(40).only()).clear();
            fail();
        } catch (NoInteractionsWanted e) {}
    }

    /**
     * This test is JUnit-specific because the code behaves different if JUnit is used.
     */
    @Test
    public void canIgnoreInvocationsWithJunit() {
        //given
        Thread t1 = new Thread() {
            @Override
            public void run() {
                mock.add("0");
                mock.add("1");
                VerificationWithTimeoutTest.this.sleep(100);
                mock.add("2");
            }
        };

        //when
        t1.start();

        //then
        verify(mock, timeout(200)).add("1");
        verify(mock, timeout(200)).add("2");
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
            // we do not need to handle this.
        }
    }

    //TODO not yet implemented
    @Ignore
    @Test
    public void shouldAllowTimeoutVerificationInOrder() throws Exception {
        //given
        Thread t1 = waitAndExerciseMock(20);

        //when
        t1.start();
        mock.add("foo");

        //then
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).add(anyString());
        inOrder.verify(mock, never()).clear();
        inOrder.verify(mock, timeout(40)).clear();
    }

    private Thread waitAndExerciseMock(final int sleep) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    exceptions.add(e);
                    throw new RuntimeException(e);
                }
                mock.clear();
            }
        };
        return t;
    }
}