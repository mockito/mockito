package org.mockito.invocation;

import org.mockito.exceptions.PrintableInvocation;

/**
 * Represent a method call on a mock.
 *
 * <p>
 *     Contains the information on the mock, the location of the stub
 *     the return value if it returned something (maybe null), or an
 *     exception if one was thrown when the method was invoked.
 * </p>
 */
public class MethodCallReport {
    private final PrintableInvocation invocation;
    private Object returnedValue;
    private Throwable throwable;
    private final String locationOfStubbing;

    /**
     * Build a new {@link MethodCallReport} with a return value.
     *
     * @param printableInvocation Information on the method call
     * @param returnedValue The value returned by the method invocation
     * @param location The location of the stub
     * @return A new MethodCallReport
     */
    public static MethodCallReport of(PrintableInvocation printableInvocation, Object returnedValue, String location) {
        return new MethodCallReport(printableInvocation, returnedValue, location);
    }

    /**
     * Build a new {@link MethodCallReport} with a return value.
     *
     * @param printableInvocation Information on the method call
     * @param throwable Tha throwable raised by the method invocation
     * @param location THe location of the stub
     * @return A new MethodCallReport
     */
    public static MethodCallReport of(PrintableInvocation printableInvocation, Throwable throwable, String location) {
        return new MethodCallReport(printableInvocation, throwable, location);
    }

    private MethodCallReport(PrintableInvocation invocation, Object returnedValue, String locationOfStubbing) {
        this.invocation = invocation;
        this.returnedValue = returnedValue;
        this.locationOfStubbing = locationOfStubbing;
    }

    private MethodCallReport(PrintableInvocation invocation, Throwable throwable, String locationOfStubbing) {
        this.invocation = invocation;
        this.throwable = throwable;
        this.locationOfStubbing = locationOfStubbing;
    }

    /**
     * @return Information on the method call, never {@code null}
     */
    public PrintableInvocation getInvocation() {
        return invocation;
    }

    /**
     * @return The resulting value of the method invocation, may be <code>null</code>
     */
    public Object getReturnedValue() {
        return returnedValue;
    }

    /**
     * @return The throwable raised by the method invocation, maybe <code>null</code>
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * @return <code>true</code> if an exception was raised, <code>false</code> otherwise
     */
    public boolean threwException() {
        return throwable != null;
    }

    /**
     * @return Location of the stub invocation
     */
    public String getLocationOfStubbing() {
        return locationOfStubbing;
    }

    // Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCallReport that = (MethodCallReport) o;

        if (invocation != null ? !invocation.equals(that.invocation) : that.invocation != null) return false;
        if (locationOfStubbing != null ? !locationOfStubbing.equals(that.locationOfStubbing) : that.locationOfStubbing != null)
            return false;
        if (returnedValue != null ? !returnedValue.equals(that.returnedValue) : that.returnedValue != null)
            return false;
        if (throwable != null ? !throwable.equals(that.throwable) : that.throwable != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = invocation != null ? invocation.hashCode() : 0;
        result = 31 * result + (returnedValue != null ? returnedValue.hashCode() : 0);
        result = 31 * result + (throwable != null ? throwable.hashCode() : 0);
        result = 31 * result + (locationOfStubbing != null ? locationOfStubbing.hashCode() : 0);
        return result;
    }
}
