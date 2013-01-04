/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.serialization;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.SimpleSerializationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class ParallelSerializationTest {

    @Test
    public void single_mock_being_serialized_and_deserialized_in_different_classloaders_by_multiple_threads() throws ExecutionException, InterruptedException {
        // given
        int iterations = 2;
        int threadingFactor = 200;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadingFactor);
        final IMethods iMethods = mock(IMethods.class, withSettings().serializable());

        // when
        for (int i = 0; i <= iterations; i++) {
            List<Future> futures = new ArrayList<Future>(threadingFactor);
            final CyclicBarrier barrier_that_will_wait_until_threads_are_ready = new CyclicBarrier(threadingFactor);

            // prepare all threads by submitting a callable
            //  - that will serialize the mock a 'threadingFactor' times
            //  - that will use the mock a 'threadingFactor' times
            for (int j = 0; j < threadingFactor; j++) {
                // submit a callable that will serialize the mock 'iMethods'
                futures.add(executorService.submit(new Callable<Object>() {
                    public Object call() throws Exception {
                        barrier_that_will_wait_until_threads_are_ready.await();
                        iMethods.arrayReturningMethod();

                        return SimpleSerializationUtil.serializeMock(iMethods).toByteArray();
                    }
                }));

                // submit a callable that will only use the mock 'iMethods'
                executorService.submit(new Callable<Object>() {
                    public Object call() throws Exception {
                        barrier_that_will_wait_until_threads_are_ready.await();
                        return iMethods.longObjectReturningMethod();
                    }
                });
            }

            // ensure we are getting the futures
            for (Future future : futures) {
                future.get();
            }
        }
    }
}
