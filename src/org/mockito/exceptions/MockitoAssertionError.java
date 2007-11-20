package org.mockito.exceptions;


public class MockitoAssertionError extends AssertionError implements HasFilterableStackTrace {

    private static final long serialVersionUID = 1L;
    protected StackTraceElement[] unfilteredStackTrace;

    public MockitoAssertionError(String message) {
        super(message);

        unfilteredStackTrace = getStackTrace();
        
        MockitoStackTraceFilter filter = new MockitoStackTraceFilter();
        filter.filterStackTrace(this);
    }
    
    public StackTraceElement[] getUnfilteredStackTrace() {
        return unfilteredStackTrace;
    }
}
