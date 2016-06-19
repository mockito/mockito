/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.concurrentmockito;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

//this test exposes the problem most of the time
public class ThreadVerifiesContinuoslyInteractingMockTest extends TestBase {

    @Mock private IMethods mock;

    @Test
    public void shouldAllowVerifyingInThreads() throws Exception {
        for(int i = 0; i < 100; i++) {
            performTest();
        }
    }

    private void performTest() throws InterruptedException {
        mock.simpleMethod();
        final Thread[] listeners = new Thread[2];
        for (int i = 0; i < listeners.length; i++) {
            final int x = i;
            listeners[i] = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(x * 10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    mock.simpleMethod();
                }
            };
            listeners[i].start();
        }
        
        verify(mock, atLeastOnce()).simpleMethod();

        for (Thread listener : listeners) {
            listener.join();
        }
    }
}