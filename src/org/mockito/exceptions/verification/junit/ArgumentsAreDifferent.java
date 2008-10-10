/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification.junit;

import java.util.Arrays;

import junit.framework.ComparisonFailure;

import org.mockito.exceptions.base.CommonStackTraceRemover;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.exceptions.base.StackTraceFilter;


public class ArgumentsAreDifferent extends ComparisonFailure implements HasStackTrace {
    
    private static final long serialVersionUID = 1L;
    private final String message;
    private StackTraceElement[] unfilteredStackTrace;

    public ArgumentsAreDifferent(String message, Throwable cause, String wanted, String actual) {
        super(message, wanted, actual);
        this.message = message;
        
        unfilteredStackTrace = getStackTrace();
        StackTraceFilter filter = new StackTraceFilter();
        filter.filterStackTrace(this);
        
        this.initCause(cause);
        CommonStackTraceRemover remover = new CommonStackTraceRemover();
        remover.remove(this, Arrays.asList(cause.getStackTrace()));
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}