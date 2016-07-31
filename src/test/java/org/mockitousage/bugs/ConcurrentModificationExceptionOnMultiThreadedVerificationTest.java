/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

// issue 322
// the only evidence of this failing test was shown on a RHEL with IBM J9 JVM 64bits
//
// details
// java.fullversion=JRE 1.6.0 IBM J9 2.6 Linux amd64-64 20111113_94967  (JIT enabled, AOT enabled)
// Linux2.6.32-220.4.2.el6.x86_64 #1SMP Mon Feb 6 16:39:28EST 2012x86_64 x86_64 x86_64 GNU/Linux
public class ConcurrentModificationExceptionOnMultiThreadedVerificationTest {

    int nThreads = 1;
    
    static final int TIMES = 100;
    static final int INTERVAL_MILLIS = 10;

    ITarget target = Mockito.mock(ITarget.class);
    ExecutorService fixedThreadPool;
    
    @Before
    public void setUp() {
        target = Mockito.mock(ITarget.class);
        fixedThreadPool = Executors.newFixedThreadPool(nThreads);
    }

    @Test
    public void shouldSuccessfullyVerifyConcurrentInvocationsWithTimeout() throws Exception {
        int potentialOverhead = 1000; // Leave 1000ms extra before timing out as leeway for test overheads
        int expectedMaxTestLength = TIMES * INTERVAL_MILLIS + potentialOverhead;

        reset(target);
        startInvocations();
        
        verify(target, timeout(expectedMaxTestLength).times(TIMES * nThreads)).targetMethod("arg");
        verifyNoMoreInteractions(target);
    }

    private void startInvocations() throws InterruptedException,
            ExecutionException {
        
        for(int i=0; i<nThreads; i++) {
            fixedThreadPool.submit(new TargetInvoker(i));
        }

    }
    
    public class TargetInvoker implements Callable<Object> {
        private final int seq;

        TargetInvoker(int seq) {
            this.seq = seq;
        }
        
        public Object call() throws Exception {
            System.err.println("started " + seq);
            for (int i = 0; i < TIMES; i++) {
                Thread.yield();
                target.targetMethod("arg");
                Thread.sleep((long) INTERVAL_MILLIS);
            }
            System.err.println("finished" + seq);
            return seq;
        }
        
    }
    
    public interface ITarget {
        String targetMethod(String arg);
    }
    
}
