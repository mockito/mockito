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

public class StrictlyMissingInvocationVerifier implements Verifier {
    
    private final Reporter reporter;
    private final ActualInvocationsFinder finder;
    
    public StrictlyMissingInvocationVerifier() {
        this(new ActualInvocationsFinder(), new Reporter());
    }
    
    public StrictlyMissingInvocationVerifier(ActualInvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }

    //TODO tests! - do you like the message?
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        //TODO push to mode
        if (!mode.strictMode() || mode.wantedCountIsZero()) {
            return;
        }
        
        List<Invocation> chunk = finder.findFirstUnverifiedChunk(invocations, wanted);
        
        if (chunk.size() == 0) {
            reporter.strictlyWantedButNotInvoked(wanted.toString());
        }
        
        if (!wanted.matches(chunk.get(0))) {
            Invocation actual = chunk.get(0);
            InvocationsPrinter printer = new InvocationsPrinter(wanted, actual);
            reporter.strictVerificationFailed(printer.printWanted(), printer.printActual(), actual.getStackTrace());
        }
    }
}