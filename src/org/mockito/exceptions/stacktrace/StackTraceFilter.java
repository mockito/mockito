package org.mockito.exceptions.stacktrace;

public interface StackTraceFilter {

    public boolean isLastStackElementToRemove(StackTraceElement e);

}
