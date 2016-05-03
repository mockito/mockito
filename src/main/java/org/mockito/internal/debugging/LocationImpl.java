/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.io.Serializable;
import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.mockito.invocation.Location;

public class LocationImpl implements Location, Serializable {

    private static final long serialVersionUID = -9054861157390980624L;
    private final Throwable stackTraceHolder;
    private final StackTraceFilter stackTraceFilter;

    public LocationImpl() {
        this(new StackTraceFilter());
    }

    public LocationImpl(StackTraceFilter stackTraceFilter) {
        this.stackTraceFilter = stackTraceFilter;
        stackTraceHolder = new Throwable();
    }

    @Override
    public String toString() {
        //TODO SF perhaps store the results after invocation?
        StackTraceElement[] filtered = stackTraceFilter.filter(stackTraceHolder.getStackTrace(), false);
        if (filtered.length == 0) {
            return "-> at <<unknown line>>";
        }
        return "-> at " + filtered[0].toString();
    }
}
