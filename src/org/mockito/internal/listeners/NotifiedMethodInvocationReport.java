/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MethodInvocationReport;

/**
 * Report on a method call
 */
public class NotifiedMethodInvocationReport implements MethodInvocationReport {
    private final Invocation invocation;
    private Object returnedValue;
    private Throwable throwable;


    /**
     * Build a new {@link org.mockito.listeners.MethodInvocationReport} with a return value.
     *
     *
     * @param invocation Information on the method call
     * @param returnedValue The value returned by the method invocation
     */
    public NotifiedMethodInvocationReport(Invocation invocation, Object returnedValue) {
        this.invocation = invocation;
        this.returnedValue = returnedValue;
    }

    /**
     * Build a new {@link org.mockito.listeners.MethodInvocationReport} with a return value.
     *
     *
     * @param invocation Information on the method call
     * @param throwable Tha throwable raised by the method invocation
     */
    public NotifiedMethodInvocationReport(Invocation invocation, Throwable throwable) {
        this.invocation = invocation;
        this.throwable = throwable;
    }

    public DescribedInvocation getInvocation() {
        return invocation;
    }

    public Object getReturnedValue() {
        return returnedValue;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean threwException() {
        return throwable != null;
    }

    public String getLocationOfStubbing() {
        return (invocation.stubInfo() == null) ? null : invocation.stubInfo().stubbedAt().toString();
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotifiedMethodInvocationReport that = (NotifiedMethodInvocationReport) o;

        if (invocation != null ? !invocation.equals(that.invocation) : that.invocation != null) return false;
        if (returnedValue != null ? !returnedValue.equals(that.returnedValue) : that.returnedValue != null)
            return false;
        if (throwable != null ? !throwable.equals(that.throwable) : that.throwable != null) return false;

        return true;
    }

    public int hashCode() {
        int result = invocation != null ? invocation.hashCode() : 0;
        result = 31 * result + (returnedValue != null ? returnedValue.hashCode() : 0);
        result = 31 * result + (throwable != null ? throwable.hashCode() : 0);
        return result;
    }
}
