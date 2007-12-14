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
    
    private final Reporter reporter = new Reporter();
    private final InvocationsCalculator calculator = new InvocationsCalculator();
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
        if (!mode.isExplicit()) {
            return;
        }
        
        int actualCount = calculator.countActual(invocations, wanted);
        Integer wantedCount = mode.wantedCount();
        boolean atLeastOnce = mode.atLeastOnceMode();
               
        if ((atLeastOnce || wantedCount == 1) && actualCount == 0) {
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
