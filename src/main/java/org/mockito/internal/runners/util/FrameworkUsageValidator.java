/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners.util;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mockito;

/**
 * Implementation of JUnit run listener that validates Mockito usage after each test
 */
public class FrameworkUsageValidator extends RunListener {
    
    private final RunNotifier notifier;
    private boolean failed;

    public FrameworkUsageValidator(RunNotifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        failed = true;
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        try {
            Mockito.validateMockitoUsage();
        } catch(Throwable t) {
            notifier.fireTestFailure(new Failure(description, t));
        }
    }

    public boolean isSussessful() {
        return !failed;
    }
}