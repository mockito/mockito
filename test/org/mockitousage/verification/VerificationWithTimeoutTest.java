/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class VerificationWithTimeoutTest extends TestBase {

    List exceptions = new LinkedList();
    
    @After
    public void after() {
        //making sure there are no threading related exceptions
        assertTrue(exceptions.isEmpty());
        exceptions.clear();
    }
    
    @Mock
    private List mock;

    @Test
    public void shouldVerifyWithTimeout() throws Exception {
        //given
        Thread t = waitAndExerciseMock(20);
        
        //when
        t.start();
        
        //then
        verify(mock, never()).clear();
        verify(mock, timeout(40)).clear();
    }

    @Test
    public void shouldFailVerificationWithTimeout() throws Exception {
        //given
        Thread t = waitAndExerciseMock(40);
        
        //when
        t.start();
        
        //then
        verify(mock, never()).clear();
        try {
            verify(mock, timeout(20)).clear();
            fail();
        } catch (MockitoAssertionError e) {}
    }
    
//    @Test
//    public void shouldAllowMixingOtherModesWithTimeout() throws Exception {
//        //given
//        Thread t = waitAndExerciseMock(40);
//        
//        //when
//        t.start();
//        
//        //then
//        verify(mock, never()).clear();
//        try {
//            verify(mock, timeout(20)).clear();
//            fail();
//        } catch (MockitoAssertionError e) {}
//    }

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