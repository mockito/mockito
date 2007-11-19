package org.mockito.exceptions;

import java.util.*;

import org.mockito.exceptions.stacktrace.StackTraceFilter;

public class MockitoAssertionError extends AssertionError {

    private static final long serialVersionUID = 1L;
    protected List<StackTraceElement> unfilteredStackTrace;

    public MockitoAssertionError(String message, StackTraceFilter filter) {
        super(message);
        
        unfilteredStackTrace = Arrays.asList(getStackTrace());
        int lastToRemove = -1;
        int i = 0;
        for (StackTraceElement trace : unfilteredStackTrace) {
            if (filter.isLastStackElementToRemove(trace)) {
                lastToRemove = i;
                break;
            }
            i++;
        }
        
        List<StackTraceElement> filtered = unfilteredStackTrace.subList(lastToRemove+1, unfilteredStackTrace.size() - 1);
        setStackTrace(filtered.toArray(new StackTraceElement[]{}));
    }
    
    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace.toArray(new StackTraceElement[]{});
    }
}
