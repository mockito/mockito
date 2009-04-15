package org.mockito.internal.debugging;

import org.mockito.exceptions.base.StackTraceFilter;

public class Location  {

    private final StackTraceElement firstTraceElement;

    public Location() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceFilter filter = new StackTraceFilter();
        this.firstTraceElement = filter.filterStackTrace(stackTrace)[0];
    }

    @Override
    public String toString() {
        return "-> at " + this.firstTraceElement.toString();
    }
}