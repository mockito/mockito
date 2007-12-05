package org.mockitousage;
import static org.junit.Assert.assertFalse;

import java.util.*;

import org.junit.Test;
import org.junit.runner.*;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.*;
import org.mockito.exceptions.*;
import org.mockito.internal.*;
import org.mockito.internal.matchers.EqualsTest;
import org.mockito.sample.MockitoSampleTest;
import org.mockitousage.binding.*;
import org.mockitousage.matchers.*;
import org.mockitousage.stubbing.*;
import org.mockitousage.verification.*;

public class MultiThreadedTest {
    
    private static class AllTestsRunner extends Thread {
        
        private boolean failed;

        public void run() {
            Result result = JUnitCore.runClasses(
                    MockitoSampleTest.class, 
                    EqualsTest.class, 
                    CglibTest.class, 
                    ExpectedInvocationTest.class, 
                    InvocationChunkTest.class, 
                    MockFactoryTest.class, 
                    MockitoBehaviorTest.class, 
                    MockitoStateTest.class, 
                    RegisteredInvocationsTest.class, 
                    ToTypeMappingsTest.class, 
                    VerifyingModeTest.class, 
                    InvocationTest.class, 
                    MockUtilTest.class, 
                    MockitoAssertionErrorTest.class, 
                    MockitoErrorTest.class, 
                    BridgeMethodPuzzleTest.class, 
                    IncorectBindingPuzzleFixedTest.class, 
                    UsingVarargsTest.class, 
                    ComparableMatchersTest.class, 
                    MatchersToStringTest.class, 
                    VerificationAndStubbingUsingMatchersTest.class, 
                    BasicStubbingTest.class, 
                    ReturningDefaultValuesTest.class, 
                    StubbingWithThrowablesTest.class, 
                    AtLeastOnceVerificationTest.class, 
                    BasicVerificationTest.class, 
                    ExactNumberOfTimesVerificationTest.class, 
                    NiceMessagesWhenVerificationFailsTest.class, 
                    NoMoreInteractionsVerificationTest.class, 
                    VerificationInOrderMixedWithOrdiraryVerificationTest.class, 
                    VerificationInOrderTest.class, 
                    VerificationOnMultipleMocksUsingMatchersTest.class, 
                    VerificationUsingMatchersTest.class, 
                    MatchersTest.class,
                    ReplacingObjectMethodsTest.class,
                    //below are tests that mess up the state
                    MockControlTest.class,
                    MockitoTest.class,
                    InvalidUsageTest.class,
                    InvalidUseOfMatchersTest.class,
                    NiceMessagesOnStrictOrderErrorsTest.class,
                    InvalidStateDetectionTest.class,
                    StackTrackeFilteringTest.class
                );
                
                if (!result.wasSuccessful()) {
                    System.err.println("Thread[" + Thread.currentThread().getId() + "]: error!");
                    List<Failure> failures = result.getFailures();
                    System.err.println(failures.size());
                    for (Failure failure : failures) {
                        System.err.println(failure.getTrace());
                        failed = true;
                    }
                }
        }

        public boolean isFailed() {
            return failed;
        }
    }
    
    @Test
    public void shouldRunInMultipleThreads() throws Exception {
        assertFalse("Run in multiple thread failed", runInMultipleThreads(4));
    }
    
    public static boolean runInMultipleThreads(int numberOfThreads) throws Exception {
        List<AllTestsRunner> threads = new LinkedList<AllTestsRunner>();
        for(int i = 1 ; i <= numberOfThreads ; i++) {
            threads.add(new AllTestsRunner());
        }
        
        for (Thread t : threads) {
            t.start();
        }
        
        boolean failed = false;        
        for (AllTestsRunner t : threads) {
            t.join();
            failed = failed? true : t.isFailed();
        }
        
        return failed;
    }
    
    public static void main(String[] args) throws Exception {
        int numberOfThreads = 100; 
        runInMultipleThreads(numberOfThreads);
        
        System.out.println("Finished running tests in " + numberOfThreads + " threads");
    }
}