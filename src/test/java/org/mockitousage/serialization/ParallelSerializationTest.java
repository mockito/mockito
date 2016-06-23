/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.serialization;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.SimpleSerializationUtil;

import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class ParallelSerializationTest {

    @Test
    public void single_mock_being_serialized_in_different_classloaders_by_multiple_threads() throws ExecutionException, InterruptedException {
        // given
        int iterations = 2;
        int threadingFactor = 200;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadingFactor);
        final IMethods iMethods_that_store_invocations = mock(IMethods.class, withSettings().serializable());

        // when
        for (int i = 0; i <= iterations; i++) {
            List<Future<?>> futures = new ArrayList<Future<?>>(threadingFactor);
            final CyclicBarrier barrier_that_will_wait_until_threads_are_ready = new CyclicBarrier(threadingFactor);

            // prepare all threads by submitting a callable
            //  - that will serialize the mock a 'threadingFactor' times
            //  - that will use the mock a 'threadingFactor' times
            for (int j = 0; j < threadingFactor; j++) {
                // submit a callable that will serialize the mock 'iMethods'
                futures.add(executorService.submit(new Callable<Object>() {
                    public Object call() throws Exception {
                        barrier_that_will_wait_until_threads_are_ready.await();

                        randomCallOn(iMethods_that_store_invocations);

                        return SimpleSerializationUtil.serializeMock(iMethods_that_store_invocations).toByteArray();
                    }
                }));

                // submit a callable that will only use the mock 'iMethods'
                executorService.submit(new Callable<Object>() {
                    public Object call() throws Exception {
                        barrier_that_will_wait_until_threads_are_ready.await();
                        return iMethods_that_store_invocations.longObjectReturningMethod();
                    }
                });
            }

            // ensure we are getting the futures
            for (Future<?> future : futures) {
                future.get();
            }
        }
    }

    private void randomCallOn(IMethods iMethods) throws CharacterCodingException {
        int random = new Random().nextInt(10);
        switch (random) {
            case 0 : iMethods.arrayReturningMethod(); break;
            case 1 : iMethods.longObjectReturningMethod(); break;
            case 2 : iMethods.linkedListReturningMethod(); break;
            case 3 : iMethods.iMethodsReturningMethod(); break;
            case 4 : iMethods.canThrowException(); break;
            case 5 : iMethods.differentMethod(); break;
            case 6 : iMethods.voidMethod(); break;
            case 7 : iMethods.varargsString(1, ""); break;
            case 8 : iMethods.forMap(null); break;
            case 9 : iMethods.throwsNothing(false); break;
            default:
        }
    }
}
