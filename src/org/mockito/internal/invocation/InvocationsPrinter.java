/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

public class InvocationsPrinter {

    private final String wanted;
    private final String actual;

    public InvocationsPrinter(InvocationMatcher wantedInvocation, Invocation actualInvocation) {
        if (wantedInvocation.matchesButMocksAreDifferent(actualInvocation)) {
            wanted = wantedInvocation.toStringWithSequenceNumber();
            actual = actualInvocation.toStringWithSequenceNumber(); 
        } else if (wantedInvocation.matchesMockArgsAndMethodNameButMethodNotEqual(actualInvocation)) {
            wanted = wantedInvocation.toStringWithArgumentTypes();
            actual = actualInvocation.toStringWithArgumentTypes();
        } else {
            wanted = wantedInvocation.toString();
            actual = actualInvocation.toString();
        }
    }

    public String printWanted() {
        return wanted;
    }

    public String printActual() {
        return actual;
    }
}