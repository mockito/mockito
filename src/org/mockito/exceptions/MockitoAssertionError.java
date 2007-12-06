/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import java.util.List;


public class MockitoAssertionError extends AssertionError implements HasStackTrace {

    private static final long serialVersionUID = 1L;
    protected StackTraceElement[] unfilteredStackTrace;

    public MockitoAssertionError(String message) {
        super(message);

        unfilteredStackTrace = getStackTrace();
        
        MockitoStackTraceFilter filter = new MockitoStackTraceFilter();
        filter.filterStackTrace(this);
    }
    
    public MockitoAssertionError(String message, List<StackTraceElement> invocationStackTrace) {
        this(message);
        
        MockitoStackTraceMerger merger = new MockitoStackTraceMerger();
        merger.merge(this, invocationStackTrace);
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}
