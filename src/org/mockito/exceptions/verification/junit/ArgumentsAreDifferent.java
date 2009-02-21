/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification.junit;

import junit.framework.ComparisonFailure;

import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.exceptions.base.StackTraceFilter;


public class ArgumentsAreDifferent extends ComparisonFailure implements HasStackTrace {
    
    private static final long serialVersionUID = 1L;
    private final String message;
    private StackTraceElement[] unfilteredStackTrace;

    public ArgumentsAreDifferent(String message, String wanted, String actual) {
        super(message, wanted, actual);
        this.message = message;
        
        unfilteredStackTrace = getStackTrace();
        StackTraceFilter filter = new StackTraceFilter();
        filter.filterStackTrace(this);
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}