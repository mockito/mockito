/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.Invocation;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Author: Szczepan Faber
 */
public class InvocationContainerImplTest {

    InvocationContainerImpl container = new InvocationContainerImpl(new ThreadSafeMockingProgress());
    Invocation invocation = new InvocationBuilder().toInvocation();
    LinkedList<Throwable> exceptions = new LinkedList<Throwable>();

    @Test
    //works 50% of the time
    public void shouldBeThreadSafe() throws Throwable {
        //given
        Thread[] t = new Thread[200];
        final CountDownLatch starter = new CountDownLatch(200);
        for (int i = 0; i < t.length; i++ ) {
            t[i] = new Thread() {
                public void run() {
                    try {
                        starter.await(); //NOPMD
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    container.setInvocationForPotentialStubbing(new InvocationMatcher(invocation));
                    container.addAnswer(new Returns("foo"));
                    container.findAnswerFor(invocation);
                }
            };
            t[i].setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e) {
                    exceptions.add(e);
                }
            });
            t[i].start();

            starter.countDown();
        }

        //when
        for (Thread aT : t) {
            aT.join();
        }

        //then
        if (exceptions.size() != 0) {
            throw exceptions.getFirst();
        }
    }

    @Test
    public void shouldReturnInvokedMock() throws Exception {
        container.setInvocationForPotentialStubbing(new InvocationMatcher(invocation));

        assertEquals(invocation.getMock(), container.invokedMock());
    }

    @Test
    public void should_tell_if_has_invocation_for_potential_stubbing() throws Exception {
        container.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());
        assertTrue(container.hasInvocationForPotentialStubbing());

        container.addAnswer(new ReturnsEmptyValues());
        assertFalse(container.hasInvocationForPotentialStubbing());
    }
}
