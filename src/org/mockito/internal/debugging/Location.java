package org.mockito.internal.debugging;

public class Location {

    private final StackTraceElement[] stackTrace;

    public Location(StackTraceElement[] stackTrace) {
        assert stackTrace != null;
        assert stackTrace.length > 0;
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return this.stackTrace[0].toString();
    }
}