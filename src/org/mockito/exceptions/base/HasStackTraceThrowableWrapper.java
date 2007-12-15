/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

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