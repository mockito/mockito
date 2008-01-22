/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.exceptions.Printable;

public class InvocationsPrinter {

    private final String wanted;
    private final String actual;

    public InvocationsPrinter(InvocationMatcher wantedInvocation, Invocation actualInvocation) {
        if (wantedInvocation.differsWithArgumentTypes(actualInvocation)) {
            wanted = wantedInvocation.toStringWithArgumentTypes();
            actual = actualInvocation.toStringWithArgumentTypes();
        } else {
            wanted = wantedInvocation.toString();
            actual = actualInvocation.toString();
        }
    }

    public Printable getWanted() {
        return new Printable() {
            public String toString() {
                return wanted;
        }};
    }

    public Printable getActual() {
        return new Printable() {
            public String toString() {
                return actual;
        }};
    }
}