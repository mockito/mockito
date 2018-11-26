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
    //Limit the amount of objects being created, as this class is heavily instantiated:
    private static final StackTraceFilter defaultStackTraceFilter = new StackTraceFilter();

    private final Throwable stackTraceHolder;
    private final StackTraceFilter stackTraceFilter;
    private final String sourceFile;

    public LocationImpl() {
        this(defaultStackTraceFilter);
    }

    public LocationImpl(StackTraceFilter stackTraceFilter) {
        this(stackTraceFilter, new Throwable());
    }

    public LocationImpl(Throwable stackTraceHolder) {
        this(defaultStackTraceFilter, stackTraceHolder);
    }

    private LocationImpl(StackTraceFilter stackTraceFilter, Throwable stackTraceHolder) {
        this.stackTraceFilter = stackTraceFilter;
        this.stackTraceHolder = stackTraceHolder;
        if (stackTraceHolder.getStackTrace() == null || stackTraceHolder.getStackTrace().length == 0) {
            //there are corner cases where exception can have a null or empty stack trace
            //for example, a custom exception can override getStackTrace() method
            this.sourceFile = "<unknown source file>";
        } else {
            this.sourceFile = stackTraceFilter.findSourceFile(stackTraceHolder.getStackTrace(), "<unknown source file>");
        }
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

    @Override
    public String getSourceFile() {
        return sourceFile;
    }
}
