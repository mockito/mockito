/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import org.mockito.internal.exceptions.base.ConditionalStackTraceFilter;


public class MockitoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private StackTraceElement[] unfilteredStackTrace;
    
    public MockitoException(String message, Throwable t) {
        super(message, t);
        filterStackTrace();
    }

    public MockitoException(String message) {
        super(message);
        filterStackTrace();
    }

    private void filterStackTrace() {
        unfilteredStackTrace = getStackTrace();
        
        ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}
