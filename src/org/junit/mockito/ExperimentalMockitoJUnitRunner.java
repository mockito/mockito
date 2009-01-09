/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.junit.mockito;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.internal.util.MockitoLoggerImpl;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Uses <b>JUnit 4.5</b> runner {@link BlockJUnit4ClassRunner}.
 * <p>
 * Does what {@link MockitoJUnitRunner} does plus warns when stubbing
 * TODO copy-paste stuff from web here  
 */
public class ExperimentalMockitoJUnitRunner extends MockitoJUnitRunner {

    private final MockitoLogger logger;
    
    public ExperimentalMockitoJUnitRunner(Class<?> klass) throws InitializationError {
        this(klass, new MockitoLoggerImpl());
    }
    
    public ExperimentalMockitoJUnitRunner(Class<?> klass, MockitoLogger logger) throws InitializationError {
        super(klass);
        this.logger = logger;
    }
    
    //this is what is really executed when the test runs
    static interface JunitTestBody {
        void run(RunNotifier notifier);
    }
    
    @Override
    public void run(RunNotifier notifier) {
        this.run(notifier, new JunitTestBody() {
            public void run(RunNotifier notifier) {
                ExperimentalMockitoJUnitRunner.super.run(notifier);
            }
        });
    }
    
    public void run(RunNotifier notifier, JunitTestBody junitTestBody) {
        MockingProgress progress = new ThreadSafeMockingProgress();
        DebuggingInfo debuggingInfo = progress.getDebuggingInfo();
        
        beforeRun(notifier, debuggingInfo);
        
        junitTestBody.run(notifier);
        
        afterRun(debuggingInfo);
    }

    private void afterRun(final DebuggingInfo debuggingInfo) {
        debuggingInfo.clearData();
    }

    private void beforeRun(RunNotifier notifier, final DebuggingInfo debuggingInfo) {
        debuggingInfo.collectData();

        RunListener listener = new RunListener() {
            @Override public void testFailure(Failure failure) throws Exception {
                debuggingInfo.printWarnings(logger);
            }
        };
        
        notifier.addListener(listener);
    }
}