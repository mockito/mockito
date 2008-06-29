/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.concurrentmockito;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockitousage.IMethods;

//this test always passes but please keep looking sys err
//this test should be run multiple times, manually
public class ThreadsShareGenerouslyStubbedMockTest extends TestBase {

    private IMethods mock;

    @Test
    public void testShouldAllowVerifyingInThreads() throws Exception {
        for(int i = 0; i < 50; i++) {
            performTest();
        }
    }

    private void performTest() throws InterruptedException {
        mock = mock(IMethods.class);
        
        stub(mock.simpleMethod("foo"))
            .toReturn("foo")
            .toReturn("bar")
            .toReturn("baz")
            .toReturn("foo")
            .toReturn("bar")
            .toReturn("baz");
        
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
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].join();
        }
    }
}