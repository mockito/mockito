/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.ActualInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsAnalyzer;
import org.mockito.internal.progress.VerificationModeImpl;

public class NumberOfInvocationsVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsAnalyzer analyzer;
    private final ActualInvocationsFinder finder;

    public NumberOfInvocationsVerifier() {
        this(new Reporter(), new InvocationsAnalyzer(), new ActualInvocationsFinder());
    }
    
    NumberOfInvocationsVerifier(Reporter reporter, InvocationsAnalyzer analyzer, ActualInvocationsFinder finder) {
        this.reporter = reporter;
        this.analyzer = analyzer;
        this.finder = finder;
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.exactNumberOfInvocationsMode()) {
            return;
        }
        
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted, mode);
        
        int actualCount = actualInvocations.size();
        if (mode.tooLittleActualInvocations(actualCount)) {
            //TODO I want a functional test that proves that correct stack trace is provided for cause for both strictly and ordinary verification
            HasStackTrace lastInvocation = analyzer.findLastMatchingInvocationTrace(actualInvocations, wanted);
            reporter.tooLittleActualInvocations(mode.wantedCount(), actualCount, wanted.toString(), lastInvocation);
        } else if (mode.tooManyActualInvocations(actualCount)) {
            //TODO I want a functional test that proves that correct stack trace is provided for cause for both strictly and ordinary verification
            HasStackTrace firstUndesired = analyzer.findFirstUndesiredInvocationTrace(actualInvocations, wanted, mode);
            reporter.tooManyActualInvocations(mode.wantedCount(), actualCount, wanted.toString(), firstUndesired);
        }
    }
}
