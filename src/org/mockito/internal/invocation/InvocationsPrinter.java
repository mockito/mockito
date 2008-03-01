/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.exceptions.Printable;

public class InvocationsPrinter {

    private final String wanted;
    private final String wantedArgs;
    private final String actualArgs;

    public InvocationsPrinter(InvocationMatcher wantedInvocation, Invocation actualInvocation) {
        wanted = wantedInvocation.getMethodName();
        if (wantedInvocation.differsWithArgumentTypes(actualInvocation)) {
            wantedArgs = wantedInvocation.getTypedArgs();
            actualArgs = actualInvocation.getTypedArgs();
        } else {
            wantedArgs = wantedInvocation.getArgs();
            actualArgs = actualInvocation.getArgs();
        }
    }

    public Printable getWanted() {
        return new PrintableString(wanted);
    }

    public Printable getWantedArgs() {
        return new PrintableString(wantedArgs);
    }

    public Printable getActualArgs() {
        return new PrintableString(actualArgs);
    }
    
    private final class PrintableString implements Printable {
        private final String printable;
        public PrintableString(String printable) {
            this.printable = printable;
        }
        public String toString() {
            return printable;
        }
    }
}