/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.progress.VerificationMode;

public class NumberOfInvocationsVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsCalculator calculator;

    public NumberOfInvocationsVerifier() {
        this(new Reporter(), new InvocationsCalculator());
    }
    
    NumberOfInvocationsVerifier(Reporter reporter, InvocationsCalculator calculator) {
        this.reporter = reporter;
        this.calculator = calculator;
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
        if (mode.atLeastOnceMode() || !mode.isExplicit()) {
            return;
        }
        
        int actualCount = calculator.countActual(invocations, wanted);
        Integer wantedCount = mode.wantedCount();
        
        if (actualCount < wantedCount) {
            HasStackTrace lastInvocation = calculator.getLastInvocationStackTrace(invocations, wanted);
            reporter.tooLittleActualInvocations(wantedCount, actualCount, wanted.toString(), lastInvocation);
        } else if (actualCount > wantedCount) {
            HasStackTrace firstUndesired = calculator.getFirstUndesiredInvocationStackTrace(invocations, wanted, mode);
            reporter.tooManyActualInvocations(wantedCount, actualCount, wanted.toString(), firstUndesired);
        }
    }
}
