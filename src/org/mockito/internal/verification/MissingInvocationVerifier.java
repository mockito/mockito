/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsAnalyzer;
import org.mockito.internal.invocation.InvocationsPrinter;
import org.mockito.internal.progress.VerificationModeImpl;

public class MissingInvocationVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsAnalyzer analyzer;
    
    public MissingInvocationVerifier() {
        this(new InvocationsAnalyzer(), new Reporter());
    }
    
    public MissingInvocationVerifier(InvocationsAnalyzer analyzer, Reporter reporter) {
        this.analyzer = analyzer;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.missingMethodMode()) {
            return;
        }
        
        int actualCount = analyzer.countActual(invocations, wanted);
        if (actualCount == 0) {
            reportMissingInvocationError(invocations, wanted);
        }
    }
    
    private void reportMissingInvocationError(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation actual = analyzer.findActualInvocation(invocations, wanted);
        
        if (actual != null) {
            InvocationsPrinter printer = new InvocationsPrinter(wanted, actual);
            reporter.wantedInvocationDiffersFromActual(printer.printWanted(), printer.printActual(), actual.getStackTrace());
        } else {
            reporter.wantedButNotInvoked(wanted.toString());
        }
    }
}
