package org.mockito.exceptions.parents;

public class HasStackTraceThrowableWrapper implements HasStackTrace {

    private final Throwable throwable;

    public HasStackTraceThrowableWrapper(Throwable throwable) {
        this.throwable = throwable;
    }
    
    public StackTraceElement[] getStackTrace() {
        return throwable.getStackTrace();
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        throwable.setStackTrace(stackTrace);
    }
}