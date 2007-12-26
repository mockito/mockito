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
import org.mockito.internal.invocation.InvocationsPrinter;
import org.mockito.internal.progress.VerificationModeImpl;

public class StrictVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsAnalyzer analyzer;
    private final ActualInvocationsFinder finder;
    
    public StrictVerifier() {
        this(new InvocationsAnalyzer(), new ActualInvocationsFinder(), new Reporter());
    }
    
    public StrictVerifier(InvocationsAnalyzer analyzer, ActualInvocationsFinder finder, Reporter reporter) {
        this.analyzer = analyzer;
        this.finder = finder;
        this.reporter = reporter;
    }

    //TODO tests! - do you like the message?
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.strictMode()) {
            return;
        }
        
        List<Invocation> chunk = finder.findFirstStrictlyUnverified(invocations, wanted);
        
        if (mode.wantedCountIsZero() && (chunk.size() == 0 || !wanted.matches(chunk.get(0)))) {
            return;
        }
        
        if (chunk.size() == 0 || !wanted.matches(chunk.get(0))) {
            //TODO got rid of those ifs... (Printer to have actual stackTrace?)
            if (chunk.size() != 0) {
                Invocation actual = chunk.get(0);
                InvocationsPrinter printer = new InvocationsPrinter(wanted, actual);
                reporter.strictVerificationFailed(printer.printWanted(), printer.printActual(), actual.getStackTrace());
            } else {
                reporter.strictlyWantedButNotInvoked(wanted.toString());
            }
        }
        
        int actualCount = chunk.size();
        
        if (mode.tooLittleActualInvocations(actualCount)) {
            HasStackTrace lastInvocation = getLastSafely(chunk);
            reporter.strictlyTooLittleActualInvocations(mode.wantedCount(), actualCount, wanted.toString(), lastInvocation);
        } else if (mode.tooManyActualInvocations(actualCount)) {
            HasStackTrace firstUndesired = chunk.get(mode.wantedCount()).getStackTrace();
            reporter.strictlyTooManyActualInvocations(mode.wantedCount(), actualCount, wanted.toString(), firstUndesired);
        }
        
        for (Invocation i : chunk) {
            i.markVerifiedStrictly();
        }
    }
    
    private HasStackTrace getLastSafely(List<Invocation> actualInvocations) {
        if (actualInvocations.isEmpty()) {
            return null;
        } else {
            return actualInvocations.get(actualInvocations.size() - 1).getStackTrace();
        }
    }
}