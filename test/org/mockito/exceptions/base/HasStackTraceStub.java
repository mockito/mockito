/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import java.util.Arrays;

public class HasStackTraceStub implements HasStackTrace {
    private StackTraceElement[] stackTrace;
    
    public HasStackTraceStub(StackTraceElement ... stackTrace) {
        this.stackTrace = stackTrace;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
    }
    
    public String toString() {
        return Arrays.toString(stackTrace);
    }
}