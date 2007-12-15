/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import java.util.Arrays;


public class MockitoAssertionError extends AssertionError implements HasStackTrace {

    private static final long serialVersionUID = 1L;
    private StackTraceElement[] unfilteredStackTrace;

    public MockitoAssertionError(String message) {
        super(message);

        unfilteredStackTrace = getStackTrace();
        
        StackTraceFilter filter = new StackTraceFilter();
        filter.filterStackTrace(this);
    }
    
    public MockitoAssertionError(String message, Throwable cause) {
        this(message);

        if (cause != null) {
            this.initCause(cause);
            CommonStackTraceRemover remover = new CommonStackTraceRemover();
            remover.remove(this, Arrays.asList(cause.getStackTrace()));
        }
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}