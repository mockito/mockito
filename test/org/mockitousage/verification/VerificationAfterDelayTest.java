/*
 * Copyright (c) 2007 Mockito contributors This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static org.fest.assertions.Assertions.*;
import static org.fest.assertions.Fail.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.StateMaster;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.configuration.ConfigurationAccess;

public class VerificationAfterDelayTest {

	@Mock
	private List<String> mock;

	private List<Exception> exceptions = new LinkedList<Exception>();	

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

    @After
    public void teardown() {
	    ConfigurationAccess.getConfig().overrideCleansStackTrace(false);
	    ConfigurationAccess.getConfig().overrideDefaultAnswer(null);
	    new StateMaster().validate();
        // making sure there are no threading related exceptions
        assertThat(exceptions).isEmpty();
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

        try{
            verify(mock, after(50).times(2)).clear();
            fail();
        } catch (MockitoAssertionError e){

        }
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
        
        assertThat(System.currentTimeMillis() - startTime).isGreaterThanOrEqualTo(100);
    }
    
    @Test(timeout=100)
    public void shouldStopEarlyIfTestIsDefinitelyFailed() throws Exception {
        // given
        Thread t = waitAndExerciseMock(50);
        
        // when
        t.start();
        
        // then
        try{
            verify(mock, after(10000).never()).clear();
            fail();
        } catch(MockitoAssertionError e){

        }
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