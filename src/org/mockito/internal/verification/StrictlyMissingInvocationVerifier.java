/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsPrinter;
import org.mockito.internal.progress.VerificationModeImpl;

public class StrictlyMissingInvocationVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    
    public StrictlyMissingInvocationVerifier() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    public StrictlyMissingInvocationVerifier(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.strictlyMissingMethodMode()) {
            return;
        }
        
        List<Invocation> chunk = finder.findFirstUnverifiedChunk(invocations, wanted);
        
        if (chunk.size() == 0) {
            reporter.strictlyWantedButNotInvoked(wanted.toString());
        } else if (!wanted.matches(chunk.get(0))) {
            Invocation actual = chunk.get(0);
            InvocationsPrinter printer = new InvocationsPrinter(wanted, actual);
            reporter.strictlyWantedDiffersFromActual(printer.printWanted(), printer.printActual(), actual.getStackTrace());
        }
    }
}