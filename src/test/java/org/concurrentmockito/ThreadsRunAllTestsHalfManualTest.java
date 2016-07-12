/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.concurrentmockito;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.MockitoTest;
import org.mockito.internal.exceptions.ReporterTest;
import org.mockito.exceptions.base.MockitoAssertionErrorTest;
import org.mockito.exceptions.base.MockitoExceptionTest;
import org.mockito.internal.AllInvocationsFinderTest;
import org.mockito.internal.InvalidStateDetectionTest;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGeneratorTest;
import org.mockito.internal.handler.MockHandlerImplTest;
import org.mockito.internal.invocation.InvocationImplTest;
import org.mockito.internal.invocation.InvocationMatcherTest;
import org.mockito.internal.invocation.InvocationsFinderTest;
import org.mockito.internal.matchers.ComparableMatchersTest;
import org.mockito.internal.matchers.EqualsTest;
import org.mockito.internal.matchers.MatchersToStringTest;
import org.mockito.internal.progress.MockingProgressImplTest;
import org.mockito.internal.progress.TimesTest;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValuesTest;
import org.mockito.internal.stubbing.defaultanswers.ReturnsGenericDeepStubsTest;
import org.mockito.internal.util.MockUtilTest;
import org.mockito.internal.util.collections.ListUtilTest;
import org.mockito.internal.verification.DefaultRegisteredInvocationsTest;
import org.mockito.internal.verification.checkers.MissingInvocationCheckerTest;
import org.mockito.internal.verification.checkers.MissingInvocationInOrderCheckerTest;
import org.mockito.internal.verification.checkers.NumberOfInvocationsCheckerTest;
import org.mockito.internal.verification.checkers.NumberOfInvocationsInOrderCheckerTest;
import org.mockitousage.basicapi.ReplacingObjectMethodsTest;
import org.mockitousage.basicapi.ResetTest;
import org.mockitousage.basicapi.UsingVarargsTest;
import org.mockitousage.examples.use.ExampleTest;
import org.mockitousage.matchers.CustomMatchersTest;
import org.mockitousage.matchers.InvalidUseOfMatchersTest;
import org.mockitousage.matchers.MatchersTest;
import org.mockitousage.matchers.VerificationAndStubbingUsingMatchersTest;
import org.mockitousage.misuse.InvalidUsageTest;
import org.mockitousage.puzzlers.BridgeMethodPuzzleTest;
import org.mockitousage.puzzlers.OverloadingPuzzleTest;
import org.mockitousage.stacktrace.ClickableStackTracesTest;
import org.mockitousage.stacktrace.PointingStackTraceToActualInvocationTest;
import org.mockitousage.stacktrace.StackTraceFilteringTest;
import org.mockitousage.stubbing.BasicStubbingTest;
import org.mockitousage.stubbing.ReturningDefaultValuesTest;
import org.mockitousage.stubbing.StubbingWithThrowablesTest;
import org.mockitousage.verification.*;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;

public class ThreadsRunAllTestsHalfManualTest extends TestBase {

    private static class AllTestsRunner extends Thread {

        private boolean failed;

        public void run() {
            Result result = JUnitCore.runClasses(
                    EqualsTest.class,
                    ListUtilTest.class,
                    MockingProgressImplTest.class,
                    TimesTest.class,
                    MockHandlerImplTest.class,
                    AllInvocationsFinderTest.class,
                    ReturnsEmptyValuesTest.class,
                    NumberOfInvocationsCheckerTest.class,
                    DefaultRegisteredInvocationsTest.class,
                    MissingInvocationCheckerTest.class,
                    NumberOfInvocationsInOrderCheckerTest.class,
                    MissingInvocationInOrderCheckerTest.class,
                    CachingMockBytecodeGeneratorTest.class,
                    InvocationMatcherTest.class,
                    InvocationsFinderTest.class,
                    InvocationImplTest.class,
                    MockitoTest.class,
                    MockUtilTest.class,
                    ReporterTest.class,
                    MockitoAssertionErrorTest.class,
                    MockitoExceptionTest.class,
                    StackTraceFilteringTest.class,
                    BridgeMethodPuzzleTest.class,
                    OverloadingPuzzleTest.class,
                    InvalidUsageTest.class,
                    UsingVarargsTest.class,
                    CustomMatchersTest.class,
                    ComparableMatchersTest.class,
                    InvalidUseOfMatchersTest.class,
                    MatchersTest.class,
                    MatchersToStringTest.class,
                    VerificationAndStubbingUsingMatchersTest.class,
                    BasicStubbingTest.class,
                    ReturningDefaultValuesTest.class,
                    StubbingWithThrowablesTest.class,
                    AtMostXVerificationTest.class,
                    BasicVerificationTest.class,
                    ExactNumberOfTimesVerificationTest.class,
                    VerificationInOrderTest.class,
                    NoMoreInteractionsVerificationTest.class,
                    SelectedMocksInOrderVerificationTest.class,
                    VerificationOnMultipleMocksUsingMatchersTest.class,
                    VerificationUsingMatchersTest.class,
                    RelaxedVerificationInOrderTest.class,
                    DescriptiveMessagesWhenVerificationFailsTest.class,
                    DescriptiveMessagesWhenTimesXVerificationFailsTest.class,
                    BasicVerificationInOrderTest.class,
                    VerificationInOrderMixedWithOrdiraryVerificationTest.class,
                    DescriptiveMessagesOnVerificationInOrderErrorsTest.class,
                    InvalidStateDetectionTest.class,
                    ReplacingObjectMethodsTest.class,
                    ClickableStackTracesTest.class,
                    ExampleTest.class,
                    PointingStackTraceToActualInvocationTest.class,
                    VerificationInOrderFromMultipleThreadsTest.class,
                    ResetTest.class,
                    ReturnsGenericDeepStubsTest.class
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
        //this test ALWAYS fails if there is a single failing unit
        assertFalse("Run in multiple thread failed", runInMultipleThreads(3));
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
            failed |= t.isFailed();
        }

        return failed;
    }

    public static void main(String[] args) throws Exception {
        int numberOfThreads = 20;
        long before = System.currentTimeMillis();
        runInMultipleThreads(numberOfThreads);
        long after = System.currentTimeMillis();
        long executionTime = (after-before)/1000;
        System.out.println("Finished tests in " + numberOfThreads + " threads in " + executionTime + " seconds.");
    }
}
