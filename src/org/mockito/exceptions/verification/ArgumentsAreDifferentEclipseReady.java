/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification;


public class ArgumentsAreDifferentEclipseReady /*extends ComparisonFailure implements HasStackTrace*/ {
    
    //coming in next release....
//
//    private static final long serialVersionUID = 1L;
//    private final String message;
//    private StackTraceElement[] unfilteredStackTrace;
//
//    public ArgumentsAreDifferentEclipseReady(String message, Throwable cause, String wanted, String actual) {
//        super(message, wanted, actual);
//        this.message = message;
//        
//        unfilteredStackTrace = getStackTrace();
//        StackTraceFilter filter = new StackTraceFilter();
//        filter.filterStackTrace(this);
//        
//        this.initCause(cause);
//        CommonStackTraceRemover remover = new CommonStackTraceRemover();
//        remover.remove(this, Arrays.asList(cause.getStackTrace()));
//    }
//    
//    @Override
//    public String getMessage() {
//        return message;
//    }
//    
//    public StackTraceElement[] getUnfilteredStackTrace() {
//        return unfilteredStackTrace;
//    }
}
