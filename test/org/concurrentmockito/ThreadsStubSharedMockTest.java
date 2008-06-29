/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.concurrentmockito;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockitousage.IMethods;

public class ThreadsStubSharedMockTest extends TestBase {

    private IMethods mock;

    @Ignore("stubbing from multiple threads is not supported")
    @Test
    public void testShouldStubFineConcurrently() throws Exception {
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
                    stub(mock.simpleMethod(getId()))
                        .toReturn(getId() + "")
                        .toReturn("foo")
                        .toReturn("bar");
                        
                    stubVoid(mock)
                        .toThrow(new RuntimeException(getId() + ""))
                        .toReturn()
                        .toThrow(new RuntimeException())
                        .on().differentMethod();
                }
            };
            listeners[i].start();
        }
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].join();
        }
    }
}