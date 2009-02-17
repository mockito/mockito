package org.mockito.internal.debugging;

import org.mockito.exceptions.base.StackTraceFilter;

public class Location {

    private final StackTraceElement[] stackTrace;

    public Location() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceFilter filter = new StackTraceFilter();
        this.stackTrace = filter.filterStackTrace(stackTrace);
    }

    @Override
    public String toString() {
        return this.stackTrace[0].toString();
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }
}