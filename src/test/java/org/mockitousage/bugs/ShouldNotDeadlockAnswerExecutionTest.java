/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//see bug 190
public class ShouldNotDeadlockAnswerExecutionTest {

    @Test
    public void failIfMockIsSharedBetweenThreads() throws Exception {
        Service service = Mockito.mock(Service.class);
        ExecutorService threads = Executors.newCachedThreadPool();
        AtomicInteger counter = new AtomicInteger(2);

        // registed answer on verySlowMethod

        Mockito.when(service.verySlowMethod()).thenAnswer(new LockingAnswer(counter));

        // execute verySlowMethod twice in separate threads

        threads.execute(new ServiceRunner(service));
        threads.execute(new ServiceRunner(service));

        // waiting for threads to finish

        threads.shutdown();

        if (!threads.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
            // threads were timed-out
            fail();
        }
    }

    @Test
    public void successIfEveryThreadHasItsOwnMock() throws Exception {
        Service service1 = Mockito.mock(Service.class);
        Service service2 = Mockito.mock(Service.class);
        ExecutorService threads = Executors.newCachedThreadPool();
        AtomicInteger counter = new AtomicInteger(2);

        // registed answer on verySlowMethod

        Mockito.when(service1.verySlowMethod()).thenAnswer(new LockingAnswer(counter));
        Mockito.when(service2.verySlowMethod()).thenAnswer(new LockingAnswer(counter));

        // execute verySlowMethod twice in separate threads

        threads.execute(new ServiceRunner(service1));
        threads.execute(new ServiceRunner(service2));

        // waiting for threads to finish

        threads.shutdown();

        if (!threads.awaitTermination(500, TimeUnit.MILLISECONDS)) {
            // threads were timed-out
            fail();
        }
    }

    static class LockingAnswer implements Answer<String> {

        private AtomicInteger counter;

        public LockingAnswer(AtomicInteger counter) {
            this.counter = counter;
        }

        /**
         * Decrement counter and wait until counter has value 0
         */
        public String answer(InvocationOnMock invocation) throws Throwable {
            counter.decrementAndGet();

            while (counter.get() != 0) {
                Thread.sleep(10);
            }

            return null;
        }

    }

    static class ServiceRunner implements Runnable {

        private Service service;

        public ServiceRunner(Service service) {
            this.service = service;
        }

        public void run() {
            service.verySlowMethod();
        }

    }

    interface Service {

        String verySlowMethod();

    }

}

