/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.mockito.invocation.Location;

import java.io.Serializable;

public class LocationImpl implements Location, Serializable {

    private static final long serialVersionUID = -9054861157390980624L;
    private final Throwable stackTraceHolder;
    private final StackTraceFilter stackTraceFilter;

    /**
     * Ugly flag to disable the collection of stack traces. When writing tests
     * you ever want to set this to false. But if you use Mockito in production
     * to create production stubs you may want to save the runtime overhead of
     * generating the stacktrace on every stub method invocation.
     */
    public static boolean ENABLE_STACKTRACE = true;

    public LocationImpl() {
        this(new StackTraceFilter());
    }

    public LocationImpl(StackTraceFilter stackTraceFilter) {
        this.stackTraceFilter = stackTraceFilter;
        if (ENABLE_STACKTRACE)
            stackTraceHolder = new Throwable();
        else
            stackTraceHolder = null;
    }

    @Override
    public String toString() {
        if (stackTraceHolder == null)
            return "-> at <<unknown line>>";
        StackTraceElement[] filtered = stackTraceFilter.filter(stackTraceHolder.getStackTrace(), false);
        if (filtered.length == 0) {
            return "-> at <<unknown line>>";
        }
        return "-> at " + filtered[0].toString();
    }
}
