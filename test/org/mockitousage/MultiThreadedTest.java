/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;
import static org.junit.Assert.assertFalse;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.MockUtilTest;
import org.mockito.MockitoTest;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.base.MockitoAssertionErrorTest;
import org.mockito.exceptions.base.MockitoExceptionTest;
import org.mockito.internal.MockHandlerTest;
import org.mockito.internal.creation.CglibTest;
import org.mockito.internal.creation.MockFactoryTest;
import org.mockito.internal.invocation.InvocationMatcherTest;
import org.mockito.internal.invocation.InvocationTest;
import org.mockito.internal.matchers.EqualsTest;
import org.mockito.internal.progress.MockingProgressImplTest;
import org.mockito.internal.progress.VerificationModeImplTest;
import org.mockito.internal.stubbing.EmptyReturnValuesTest;
import org.mockito.internal.verification.NumberOfInvocationsVerifierTest;
import org.mockitousage.binding.BridgeMethodPuzzleTest;
import org.mockitousage.binding.IncorectBindingPuzzleFixedTest;
import org.mockitousage.matchers.ComparableMatchersTest;
import org.mockitousage.matchers.InvalidUseOfMatchersTest;
import org.mockitousage.matchers.MatchersTest;
import org.mockitousage.matchers.MatchersToStringTest;
import org.mockitousage.matchers.VerificationAndStubbingUsingMatchersTest;
import org.mockitousage.sample.MockitoSampleTest;
import org.mockitousage.stubbing.BasicStubbingTest;
import org.mockitousage.stubbing.ReturningDefaultValuesTest;
import org.mockitousage.stubbing.StubbingWithThrowablesTest;
import org.mockitousage.verification.AtLeastOnceVerificationTest;
import org.mockitousage.verification.BasicVerificationTest;
import org.mockitousage.verification.DescriptiveMessagesOnStrictOrderErrorsTest;
import org.mockitousage.verification.DescriptiveMessagesWhenVerificationFailsTest;
import org.mockitousage.verification.ExactNumberOfTimesVerificationTest;
import org.mockitousage.verification.NoMoreInteractionsVerificationTest;
import org.mockitousage.verification.StrictVerificationMixedWithOrdiraryVerificationTest;
import org.mockitousage.verification.StrictVerificationTest;
import org.mockitousage.verification.VerificationOnMultipleMocksUsingMatchersTest;
import org.mockitousage.verification.VerificationUsingMatchersTest;

public class MultiThreadedTest extends RequiresValidState {
    
    private static class AllTestsRunner extends Thread {
        
        private boolean failed;

        public void run() {
            Result result = JUnitCore.runClasses(
                    MockitoSampleTest.class, 
                    EqualsTest.class, 
                    CglibTest.class, 
                    InvocationMatcherTest.class, 
                    MockFactoryTest.class, 
                    NumberOfInvocationsVerifierTest.class, 
                    MockingProgressImplTest.class, 
                    EmptyReturnValuesTest.class, 
                    VerificationModeImplTest.class, 
                    InvocationTest.class, 
                    MockUtilTest.class, 
                    MockitoAssertionErrorTest.class, 
                    MockitoExceptionTest.class, 
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
                    DescriptiveMessagesWhenVerificationFailsTest.class, 
                    NoMoreInteractionsVerificationTest.class, 
                    StrictVerificationMixedWithOrdiraryVerificationTest.class, 
                    StrictVerificationTest.class, 
                    VerificationOnMultipleMocksUsingMatchersTest.class, 
                    VerificationUsingMatchersTest.class, 
                    MatchersTest.class,
                    ReplacingObjectMethodsTest.class,
                    //below are tests that mess up the state
                    MockHandlerTest.class,
                    MockitoTest.class,
                    InvalidUsageTest.class,
                    InvalidUseOfMatchersTest.class,
                    DescriptiveMessagesOnStrictOrderErrorsTest.class,
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
        for (int i = 1; i <= numberOfThreads; i++) {
            threads.add(new AllTestsRunner());
        }

        for (Thread t : threads) {
            t.start();
        }

        boolean failed = false;
        for (AllTestsRunner t : threads) {
            t.join();
            failed = failed ? true : t.isFailed();
        }
        
        return failed;
    }
    
    public static void main(String[] args) throws Exception {
        int numberOfThreads = 100; 
        runInMultipleThreads(numberOfThreads);
        
        System.out.println("Finished running tests in " + numberOfThreads + " threads");
    }
}