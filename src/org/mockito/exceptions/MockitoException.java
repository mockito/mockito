/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

public class MockitoException extends RuntimeException implements HasStackTrace {

    private static final long serialVersionUID = 1L;

    protected StackTraceElement[] unfilteredStackTrace;

    public MockitoException(String message) {
        super(message);

        unfilteredStackTrace = getStackTrace();
        
        MockitoStackTraceFilter filter = new MockitoStackTraceFilter();
        filter.filterStackTrace(this);
    }
    
    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}
