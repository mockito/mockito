/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;



public class MockitoAssertionError extends AssertionError {

    private static final long serialVersionUID = 1L;
    private StackTraceElement[] unfilteredStackTrace;

    public MockitoAssertionError(String message) {
        super(message);

        unfilteredStackTrace = getStackTrace();
        
        StackTraceFilter filter = new StackTraceFilter();
        filter.filterConditionally(this);
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}