/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import java.io.Serializable;

import org.mockito.internal.exceptions.base.StackTraceFilter;

public class Location implements Serializable {

    private static final long serialVersionUID = -9054861157390980624L;
    private final String where;

    public Location() {
        this(new StackTraceFilter());
    }

    public Location(StackTraceFilter filter) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement[] filtered = filter.filter(stackTrace, false);
        if (filtered.length == 0) {
            where = "-> at <<unknown line>>";
        } else {
            where = "-> at " + filtered[0].toString();
        }
    }

    @Override
    public String toString() {
        return where;
    }
}