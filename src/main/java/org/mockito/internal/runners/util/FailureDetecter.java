/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners.util;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Implementation of JUnit run listener that knows when any of the tests failed
 */
public class FailureDetecter extends RunListener {

    private boolean failed;

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        failed = true;
    }

    public boolean isSussessful() {
        return !failed;
    }
}