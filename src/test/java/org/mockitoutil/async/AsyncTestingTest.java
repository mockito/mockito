/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil.async;

import org.junit.After;
import org.junit.Test;
import org.mockitoutil.Stopwatch;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.mockitoutil.Stopwatch.createNotStarted;

public class AsyncTestingTest {

    private AsyncTesting async = new AsyncTesting();
    private Stopwatch watch = createNotStarted();

    @After
    public void after() {
        async.cleanUp();
    }

    @Test
    public void sanity_test() {
        //given
        watch.start();
        final AtomicInteger value = new AtomicInteger(0);

        //when
        async.runAfter(200, new Runnable() {
            public void run() {
                value.incrementAndGet();
            }
        });

        //then the runnable is truly async and has not ran yet:
        assertEquals(0, value.get());

        //after some wait...
        watch.waitFor(300);

        //we actually waited for some time
        watch.assertElapsedTimeIsMoreThan(200, MILLISECONDS);

        //and the async has actually ran:
        assertEquals(1, value.get());
    }
}
