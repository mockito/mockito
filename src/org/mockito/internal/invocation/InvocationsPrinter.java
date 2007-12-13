package org.mockito.internal.invocation;

public class InvocationsPrinter {

    private final String wanted;
    private final String actual;

    public InvocationsPrinter(InvocationMatcher wantedInvocation, Invocation actualInvocation) {
        if (wantedInvocation.matchesButNotMethodDeclaredClass(actualInvocation)) {
            wanted = wantedInvocation.toStringWithArgumentTypes();
            actual = actualInvocation.toStringWithArgumentTypes();
        } else if (wantedInvocation.matchesButMocksAreDifferent(actualInvocation)) {
            wanted = wantedInvocation.toStringWithSequenceNumber();
            actual = actualInvocation.toStringWithSequenceNumber();
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