/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.ActualInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsPrinter;
import org.mockito.internal.progress.VerificationModeImpl;

public class MissingInvocationVerifier implements Verifier {
    
    private final Reporter reporter;
    private final ActualInvocationsFinder finder;
    
    public MissingInvocationVerifier() {
        this(new ActualInvocationsFinder(), new Reporter());
    }
    
    public MissingInvocationVerifier(ActualInvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.missingMethodMode()) {
            return;
        }
        
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted, mode);
        
        if (actualInvocations.size() == 0) {
            Invocation similar = finder.findSimilarInvocation(invocations, wanted, mode);
            reportMissingInvocationError(wanted, similar);
        }
    }

    private void reportMissingInvocationError(InvocationMatcher wanted, Invocation similar) {
        if (similar != null) {
            InvocationsPrinter printer = new InvocationsPrinter(wanted, similar);
            reporter.wantedInvocationDiffersFromActual(printer.printWanted(), printer.printActual(), similar.getStackTrace());
        } else {
            reporter.wantedButNotInvoked(wanted.toString());
        }
    }
}