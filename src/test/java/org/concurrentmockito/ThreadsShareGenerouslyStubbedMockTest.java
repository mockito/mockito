/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.concurrentmockito;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//this test always passes but please keep looking sys err
//this test should be run 10 times, manually
public class ThreadsShareGenerouslyStubbedMockTest extends TestBase {

    private IMethods mock;

    @Test
    public void shouldAllowVerifyingInThreads() throws Exception {
        for(int i = 0; i < 50; i++) {
            performTest();
        }
    }

    private void performTest() throws InterruptedException {
        mock = mock(IMethods.class);
        
        when(mock.simpleMethod("foo"))
            .thenReturn("foo")
            .thenReturn("bar")
            .thenReturn("baz")
            .thenReturn("foo")
            .thenReturn("bar")
            .thenReturn("baz");
        
        final Thread[] listeners = new Thread[100];
        for (int i = 0; i < listeners.length; i++) {
            listeners[i] = new Thread() {
                @Override
                public void run() {
                    try {
                        mock.simpleMethod("foo");
                        mock.simpleMethod("foo");
                        mock.simpleMethod("foo");
                        mock.simpleMethod("foo");
                        mock.simpleMethod("foo");
                        mock.simpleMethod("foo");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            listeners[i].start();
        }
        for (Thread listener : listeners) {
            listener.join();
        }
    }
}