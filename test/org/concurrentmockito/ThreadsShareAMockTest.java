/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.concurrentmockito;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ThreadsShareAMockTest extends TestBase {

    private IMethods mock;

    @Test
    public void shouldAllowVerifyingInThreads() throws Exception {
        for(int i = 0; i < 100; i++) {
            performTest();
        }
    }

    private void performTest() throws InterruptedException {
        mock = mock(IMethods.class);
        final Thread[] listeners = new Thread[3];
        for (int i = 0; i < listeners.length; i++) {
            listeners[i] = new Thread() {
                @Override
                public void run() {
                    mock.simpleMethod("foo");
                }
            };
            listeners[i].start();
        }
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].join();
        }
        verify(mock, times(listeners.length)).simpleMethod("foo");
    }
}