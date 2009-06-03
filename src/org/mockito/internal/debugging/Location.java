/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.internal.exceptions.base.StackTraceFilter;

public class Location  {

    private final StackTraceElement firstTraceElement;

    public Location() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceFilter filter = new StackTraceFilter();
        this.firstTraceElement = filter.filter(stackTrace, false)[0];
    }

    @Override
    public String toString() {
        return "-> at " + this.firstTraceElement.toString();
    }
}