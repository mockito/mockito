/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.invocation.InvocationsPrinter;
import org.mockito.internal.progress.VerificationMode;

public class MissingInvocationVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsCalculator calculator;
    
    public MissingInvocationVerifier() {
        this(new InvocationsCalculator(), new Reporter());
    }
    
    public MissingInvocationVerifier(InvocationsCalculator calculator, Reporter reporter) {
        this.calculator = calculator;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
        if (!mode.missingMethodMode()) {
            return;
        }
        
        int actualCount = calculator.countActual(invocations, wanted);
        if (actualCount == 0) {
            reportMissingInvocationError(invocations, wanted);
        }
    }
    
    private void reportMissingInvocationError(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation actual = calculator.findActualInvocation(invocations, wanted);
        
        if (actual != null) {
            InvocationsPrinter printer = new InvocationsPrinter(wanted, actual);
            reporter.wantedInvocationDiffersFromActual(printer.printWanted(), printer.printActual(), actual.getStackTrace());
        } else {
            reporter.wantedButNotInvoked(wanted.toString());
        }
    }
}
