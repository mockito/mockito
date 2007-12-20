/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsAnalyzer;
import org.mockito.internal.progress.VerificationModeImpl;

public class NumberOfInvocationsVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsAnalyzer analyzer;

    public NumberOfInvocationsVerifier() {
        this(new Reporter(), new InvocationsAnalyzer());
    }
    
    NumberOfInvocationsVerifier(Reporter reporter, InvocationsAnalyzer analyzer) {
        this.reporter = reporter;
        this.analyzer = analyzer;
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.exactNumberOfInvocationsMode()) {
            return;
        }
        
        //get list of actual invocations (first matching chunk if strictly)
        //if list size differs report
        //if not mark as verified
        
        int actualCount = analyzer.countActual(invocations, wanted);
        int wantedCount = mode.wantedCount();
        
        if (actualCount < wantedCount) {
            HasStackTrace lastInvocation = analyzer.findLastMatchingInvocationTrace(invocations, wanted);
            reporter.tooLittleActualInvocations(wantedCount, actualCount, wanted.toString(), lastInvocation);
        } else if (actualCount > wantedCount) {
            HasStackTrace firstUndesired = analyzer.findFirstUndesiredInvocationTrace(invocations, wanted, mode);
            reporter.tooManyActualInvocations(wantedCount, actualCount, wanted.toString(), firstUndesired);
        }
    }
}
