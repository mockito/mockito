package org.mockito.internal.debugging;

import org.mockito.exceptions.base.StackTraceFilter;

public class Localized<T> {

    private final T object;
    private StackTraceElement[] stackTrace;

    public Localized(T object) {
        this.object = object;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceFilter filter = new StackTraceFilter();
        this.stackTrace = filter.filterStackTrace(stackTrace);
    }

    public T getObject() {
        return object;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public Location getLocation() {
        return new Location(stackTrace);
    }
}