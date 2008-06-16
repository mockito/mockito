package org.mockito.internal.verification;

import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class SyncingPrinter {

    private final String wanted;
    private final String actual;

    public SyncingPrinter(InvocationMatcher wanted, Invocation actual) {
        if (wanted.hasMultilinePrint() || actual.hasMultiLinePrint()) {
            this.wanted = wanted.toMultilineString();
            this.actual = actual.toMultilineString();
        } else {
            this.wanted = wanted.toString();
            this.actual = actual.toString();
        }
    }
    
    public PrintableInvocation getWanted() {
        return new PrintableInvocation() {
            public String toString() {
                return wanted;
            }
        };
    }
    
    public PrintableInvocation getActual() {
        return new PrintableInvocation() {
            public String toString() {
                return actual;
            }
        };
    }
}
