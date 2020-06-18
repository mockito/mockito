/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.io.Serializable;

import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.mockito.invocation.Location;

public class LocationImpl implements Location, Serializable {

    private static final long serialVersionUID = -9054861157390980624L;
    // Limit the amount of objects being created, as this class is heavily instantiated:
    private static final StackTraceFilter stackTraceFilter = new StackTraceFilter();

    private String stackTraceLine;
    private String sourceFile;

    public LocationImpl() {
        this(new Throwable(), false);
    }

    public LocationImpl(Throwable stackTraceHolder, boolean isInline) {
        this(stackTraceFilter, stackTraceHolder, isInline);
    }

    public LocationImpl(StackTraceFilter stackTraceFilter) {
        this(stackTraceFilter, new Throwable(), false);
    }

    private LocationImpl(
            StackTraceFilter stackTraceFilter, Throwable stackTraceHolder, boolean isInline) {
        computeStackTraceInformation(stackTraceFilter, stackTraceHolder, isInline);
    }

    @Override
    public String toString() {
        return stackTraceLine;
    }

    /**
     * Eagerly compute the stacktrace line from the stackTraceHolder. Storing the Throwable is
     * memory-intensive for tests that have large stacktraces and have a lot of invocations on
     * mocks.
     */
    private void computeStackTraceInformation(
            StackTraceFilter stackTraceFilter, Throwable stackTraceHolder, boolean isInline) {
        StackTraceElement filtered = stackTraceFilter.filterFirst(stackTraceHolder, isInline);

        // there are corner cases where exception can have a null or empty stack trace
        // for example, a custom exception can override getStackTrace() method
        if (filtered == null) {
            this.stackTraceLine = "-> at <<unknown line>>";
            this.sourceFile = "<unknown source file>";
        } else {
            this.stackTraceLine = "-> at " + filtered.toString();
            this.sourceFile = filtered.getFileName();
        }
    }

    @Override
    public String getSourceFile() {
        return sourceFile;
    }
}
