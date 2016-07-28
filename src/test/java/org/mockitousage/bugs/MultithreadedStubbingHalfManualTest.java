/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import static java.util.Collections.synchronizedList;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class MultithreadedStubbingHalfManualTest {

    /**
     * Class with two methods, one of them is repeatedly mocked while another is repeatedly called.
     */
    public interface ToMock {
        Integer getValue(Integer param);

        List<Integer> getValues(Integer param);
    }

    /**
     * Thread pool for concurrent invocations.
     */
    private Executor executor;

    private List<Exception> exceptions = synchronizedList(new LinkedList<Exception>());

    @Before
    public void setUp() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * The returned runnable simply calls ToMock.getValues(int).
     *
     * @param toMock The mocked object
     * @return The runnable.
     */
    private Runnable getConflictingRunnable(final ToMock toMock) {
        return new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep((long) (Math.random() * 10));
                    } catch (InterruptedException e) {
                    }
                    if (!toMock.getValues(0).isEmpty()) {
                        fail("Shouldn't happen, were just making sure it wasn't optimized away...");
                    }
                }
            }
        };
    }

    @Test
    //this problem shows at 4 out of 5 executions
    //it is not strictly a bug because Mockito does not support simultanous stubbing (see FAQ)
    //however I decided to synchronize some calls in order to make the exceptions nicer 
    public void tryToRevealTheProblem() {
        ToMock toMock = mock(ToMock.class);
        for (int i = 0; i < 100; i++) {
            int j = i % 11;

            // Repeated mocking
            when(toMock.getValue(i)).thenReturn(j);
            //TODO make it also showing errors for doReturn()
//            doReturn(j).when(toMock).getValue(i);

            while (true) {
                try {
                    // Scheduling invocation
                    this.executor.execute(getConflictingRunnable(toMock));
                    break;
                } catch (RejectedExecutionException ex) {
                    fail();
                }
            }

            try {
                Thread.sleep(10 / ((i % 10) + 1)); //NOPMD
            } catch (InterruptedException e) {
            }
        }
    }
}