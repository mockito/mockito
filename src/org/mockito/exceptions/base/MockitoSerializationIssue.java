/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.base;

import java.io.ObjectStreamException;

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
 * @since 1.10.0
 */
public class MockitoSerializationIssue extends ObjectStreamException {

    private static final long serialVersionUID = 5195617906606661289L;
    private StackTraceElement[] unfilteredStackTrace;

    public MockitoSerializationIssue(final String message, final Exception cause) {
        super(message);
        initCause(cause);
        filterStackTrace();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        filterStackTrace();
        return super.getStackTrace();
    }

    private void filterStackTrace() {
        unfilteredStackTrace = super.getStackTrace();

        final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}
