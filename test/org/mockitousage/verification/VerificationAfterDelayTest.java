/*
 * Copyright (c) 2007 Mockito contributors This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockitoutil.TestBase;

public class VerificationAfterDelayTest extends TestBase {
    
    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Mock
    private List<String> mock;

    private List<Exception> exceptions = new LinkedList<Exception>();

    @After
    public void teardown() {
        // making sure there are no threading related exceptions
        assertTrue(exceptions.isEmpty());
    }

    @Test
    public void shouldVerifyNormallyWithSpecificTimes() throws Exception {
        // given
        Thread t = waitAndExerciseMock(20);

        // when
        t.start();

        // then
        verify(mock, after(50).times(1)).clear();
    }

    @Test
    public void shouldVerifyNormallyWithAtLeast() throws Exception {
        // given
        Thread t = waitAndExerciseMock(20);

        // when
        t.start();

        // then
        verify(mock, after(100).atLeast(1)).clear();
    }

    @Test
    public void shouldFailVerificationWithWrongTimes() throws Exception {
        // given
        Thread t = waitAndExerciseMock(20);

        // when
        t.start();

        // then
        verify(mock, times(0)).clear();
        
        expected.expect(MockitoAssertionError.class);
        verify(mock, after(50).times(2)).clear();
    }

    @Test
    public void shouldWaitTheFullTimeIfTheTestCouldPass() throws Exception {
        // given
        Thread t = waitAndExerciseMock(50);

        // when
        t.start();

        // then        
        long startTime = System.currentTimeMillis();
        
        try {
            verify(mock, after(100).atLeast(2)).clear();
            fail();
        } catch (MockitoAssertionError e) {}
        
        assertTrue(System.currentTimeMillis() - startTime >= 100);
    }
    
    @Test(timeout=100)
    public void shouldStopEarlyIfTestIsDefinitelyFailed() throws Exception {
        // given
        Thread t = waitAndExerciseMock(50);
        
        // when
        t.start();
        
        // then
        expected.expect(MockitoAssertionError.class);
        verify(mock, after(10000).never()).clear();
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