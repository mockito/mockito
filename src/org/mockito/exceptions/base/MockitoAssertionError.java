/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.base;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;



public class MockitoAssertionError extends AssertionError {

    private static final long serialVersionUID = 1L;
    private final StackTraceElement[] unfilteredStackTrace;

    public MockitoAssertionError(String message) {
        super(message);

        unfilteredStackTrace = getStackTrace();
        
        ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }
    
    /**
     * Creates a copy of the given assertion error with the custom failure message prepended.
     * @param error The assertion error to copy
     * @param message The custom message to prepend
     * @since 2.0.0
     */
    public MockitoAssertionError(MockitoAssertionError error, String message) {
        super(message + "\n" + error.getMessage());
        super.setStackTrace(error.getStackTrace());
        unfilteredStackTrace = error.getUnfilteredStackTrace();
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}