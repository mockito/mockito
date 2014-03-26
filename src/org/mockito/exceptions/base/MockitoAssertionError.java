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

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}