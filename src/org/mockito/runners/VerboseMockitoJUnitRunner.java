/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockito.internal.debugging.WarningsCollector;
import org.mockito.internal.debugging.WarningsPrinterImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.UnusedStubsFinder;
import org.mockito.internal.listeners.CollectCreatedMocks;
import org.mockito.internal.listeners.MockingStartedListener;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.RunnerImpl;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * Experimental implementation that suppose to improve tdd/testing experience. 
 * Don't hesitate to send feedback to mockito@googlegroups.com
 * <b>It is very likely it will change in the next version!</b>
 * <p>
 * This runner does exactly what {@link MockitoJUnitRunner} does but also  
 * adds extra Mocktio hints to the exception message. 
 * The point is that Mockito should help the tdd developer to quickly figure out if the test fails for the right reason and track the reason. 
 * <p>
 * The implemntation is pretty hacky - it uses brute force of reflection to modify the exception message and add extra mockito hints.
 * You've been warned. 
 * <p>
 * Do you think it is useful or not? Drop us an email at mockito@googlegroups.com
 * <p>
 * Experimental implementation - will change in future!
 */
public class VerboseMockitoJUnitRunner extends Runner {

    private RunnerImpl runner;
    
    public VerboseMockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        this(new RunnerFactory().create(klass));
    }
    
    VerboseMockitoJUnitRunner(RunnerImpl runnerImpl) {
        this.runner = runnerImpl;
    }
    
    @Override
    public void run(RunNotifier notifier) {        

        //a listener that changes the failure's exception in a very hacky way...
        RunListener listener = new RunListener() {
            
            WarningsCollector warningsCollector;
                       
            @Override
            public void testStarted(Description description) throws Exception {
                warningsCollector = new WarningsCollector();
            }
            
            @Override 
            public void testFailure(final Failure failure) throws Exception {       
                String warnings = warningsCollector.getWarnings();
                
                //TODO: this has to protect the use in case jUnit changes and this internal state logic fails
                Throwable throwable = (Throwable) Whitebox.getInternalState(failure, "fThrownException");

                String newMessage = "contains both: actual test failure *and* Mockito warnings.\n" +
                        warnings + "\n *** The actual failure is because of: ***\n";

                ExceptionIncludingMockitoWarnings e = new ExceptionIncludingMockitoWarnings(newMessage, throwable);
                e.setStackTrace(throwable.getStackTrace());
                Whitebox.setInternalState(failure, "fThrownException", e);
            }
        };

        notifier.addFirstListener(listener);

        runner.run(notifier);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }
}