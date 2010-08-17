package org.mockito.internal.stubbing;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.answers.Returns;

import java.util.LinkedList;
import java.util.List;

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
        for (int i = 0; i < t.length; i++ ) {
            t[i] = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(10);
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
        }

        //when
        for (int i = 0; i < t.length; i++ ) {
            t[i].join();
        }

        //then
        if (exceptions.size() != 0) {
            throw exceptions.getFirst();
        }
    }
}
