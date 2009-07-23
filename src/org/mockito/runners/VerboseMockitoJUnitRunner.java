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
import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.RunnerImpl;
import org.mockito.internal.util.reflection.Whitebox;

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
    
    public VerboseMockitoJUnitRunner(Class<?> klass) {
        this(klass, new RunnerFactory().create(klass));
    }
    
    VerboseMockitoJUnitRunner(Class<?> klass, RunnerImpl runnerImpl) {
        this.runner = runnerImpl;
    }
    
    @Override
    public void run(RunNotifier notifier) {
        MockingProgress progress = new ThreadSafeMockingProgress();
        DebuggingInfo debuggingInfo = progress.getDebuggingInfo();
        
        beforeRun(notifier, debuggingInfo);
        
        runner.run(notifier);
        
        afterRun(debuggingInfo);
    }

    private void afterRun(final DebuggingInfo debuggingInfo) {
        debuggingInfo.clearData();
    }

    private void beforeRun(RunNotifier notifier, final DebuggingInfo debuggingInfo) {
        debuggingInfo.collectData();

        //a listener that changes the failure's exception in a very hacky way...
        RunListener listener = new RunListener() {
            @Override public void testFailure(final Failure failure) throws Exception {
                Throwable throwable = (Throwable) Whitebox.getInternalState(failure, "fThrownException");
                
                String newMessage = throwable.getMessage();
                newMessage += "\n" + debuggingInfo.getWarnings(false) + "\n*** The actual failure is because of: ***\n";
                
                Whitebox.setInternalState(throwable, "detailMessage", newMessage);
            }
        };
        
        notifier.addFirstListener(listener);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }
}