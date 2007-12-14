/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.exceptions.Exceptions;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.invocation.InvocationsPrinter;
import org.mockito.internal.progress.OngoingVerifyingMode;

public class MissingInvocationVerifier implements Verifier {

    public void verify(InvocationsCalculator calculator, InvocationMatcher wanted, OngoingVerifyingMode mode) {
        int actualCount = calculator.countActual(wanted);
        Integer wantedCount = mode.wantedCount();
        boolean atLeastOnce = mode.atLeastOnceMode();
               
        if ((atLeastOnce || wantedCount == 1) && actualCount == 0) {
            reportMissingInvocationError(calculator, wanted);
        }
    }
    
    private void reportMissingInvocationError(InvocationsCalculator calculator, InvocationMatcher wanted) {
        Invocation actual = calculator.findActualInvocation(wanted);
        
        if (actual != null) {
            InvocationsPrinter printer = new InvocationsPrinter(wanted, actual);
            Exceptions.wantedInvocationDiffersFromActual(printer.printWanted(), printer.printActual(), actual.getStackTrace());
        } else {
            Exceptions.wantedButNotInvoked(wanted.toString());
        }
    }
}
