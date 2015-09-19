/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.base;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;


/**
 * Raised by mockito to emit an error either due to Mockito, or due to the User.
 *
 * <p>
 *     The stack trace is filtered from mockito calls if you are using {@link #getStackTrace()}.
 *     For debugging purpose though you can still access the full stacktrace using {@link #getUnfilteredStackTrace()}.
 *     However note that other calls related to the stackTrace will refer to the filter stacktrace.
 * </p>
 *
 */
public class MockitoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private StackTraceElement[] unfilteredStackTrace;

    // TODO lazy filtered stacktrace initialization
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
