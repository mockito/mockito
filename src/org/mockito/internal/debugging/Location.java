package org.mockito.internal.debugging;

import org.mockito.exceptions.base.StackTraceFilter;

public class Location  {

    private final StackTraceElement firstTraceElement;

    public Location() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceFilter filter = new StackTraceFilter();
        this.firstTraceElement = filter.filterStackTrace(stackTrace)[0];
    }

    public Location(StackTraceElement firstTraceElement) {
        this.firstTraceElement = firstTraceElement;
    }

    @Override
    public String toString() {
        return this.firstTraceElement.toString();
    }

    //TODO this needs to refactored - I don't want to talk to StackTraceElements any more
    public StackTraceElement[] getStackTrace() {
        return new StackTraceElement[] {firstTraceElement};
    }
}