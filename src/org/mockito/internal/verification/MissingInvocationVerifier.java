/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.progress.VerificationModeImpl;

public class MissingInvocationVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    
    public MissingInvocationVerifier() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    public MissingInvocationVerifier(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.missingMethodMode()) {
            return;
        }
        
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted, mode);
        
        if (actualInvocations.isEmpty()) {
            Invocation similar = finder.findSimilarInvocation(invocations, wanted, mode);
            reportMissingInvocationError(wanted, similar);
        }
    }

    private void reportMissingInvocationError(InvocationMatcher wanted, Invocation similar) {
        if (similar != null) {
            SyncingPrinter syncingPrinter = new SyncingPrinter(wanted, similar);
            reporter.argumentsAreDifferent(syncingPrinter.getWanted(), syncingPrinter.getActual(), similar.getStackTrace());
        } else {
            reporter.wantedButNotInvoked(wanted);
        }
    }
}