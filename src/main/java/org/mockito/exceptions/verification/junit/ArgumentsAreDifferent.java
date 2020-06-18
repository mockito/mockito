/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification.junit;

import static org.mockito.internal.util.StringUtil.removeFirstLine;

import junit.framework.ComparisonFailure;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;

public class ArgumentsAreDifferent extends ComparisonFailure {

    private static final long serialVersionUID = 1L;
    private final String message;
    private final StackTraceElement[] unfilteredStackTrace;

    public ArgumentsAreDifferent(String message, String wanted, String actual) {
        super(message, wanted, actual);
        this.message = message;

        unfilteredStackTrace = getStackTrace();
        ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }

    @Override
    public String toString() {
        return removeFirstLine(super.toString());
    }
}
