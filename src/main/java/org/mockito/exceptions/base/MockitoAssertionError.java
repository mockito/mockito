/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.base;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;

/**
 * Base class for verification errors emitted by Mockito.
 * Verification errors are triggered by "verify" methods,
 * for example {@link org.mockito.Mockito#verify(Object)} or {@link org.mockito.Mockito#verifyNoMoreInteractions(Object...)}.
 * All error classes that inherit from this class will have the stack trace filtered.
 * Filtering removes Mockito internal stack trace elements to provide clean stack traces and improve productivity.
 * <p>
 * The stack trace is filtered from mockito calls if you are using {@link #getStackTrace()}.
 * For debugging purpose though you can still access the full stacktrace using {@link #getUnfilteredStackTrace()}.
 * However note that other calls related to the stackTrace will refer to the filter stacktrace.
 * <p>
 * Advanced users and framework integrators can control stack trace filtering behavior
 * via {@link org.mockito.plugins.StackTraceCleanerProvider} classpath plugin.
 */
public class MockitoAssertionError extends AssertionError {

    private static final long serialVersionUID = 1L;
    private final StackTraceElement[] unfilteredStackTrace;

    public MockitoAssertionError(String message) {
        super(message);

        unfilteredStackTrace = getStackTrace();

        ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }

    /**
     * Creates a copy of the given assertion error with the custom failure message prepended.
     * @param error The assertion error to copy
     * @param message The custom message to prepend
     * @since 2.1.0
     */
    public MockitoAssertionError(MockitoAssertionError error, String message) {
        super(message + "\n" + error.getMessage());
        super.setStackTrace(error.getStackTrace());
        unfilteredStackTrace = error.getUnfilteredStackTrace();
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}
