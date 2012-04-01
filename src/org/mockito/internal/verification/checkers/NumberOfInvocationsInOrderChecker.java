/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class NumberOfInvocationsInOrderChecker {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    private final InvocationMarker invocationMarker = new InvocationMarker();
    
    public NumberOfInvocationsInOrderChecker() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    NumberOfInvocationsInOrderChecker(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount, InOrderContext context) {
        List<Invocation> chunk = finder.findMatchingChunk(invocations, wanted, wantedCount, context);
        
        int actualCount = chunk.size();
        
        if (wantedCount > actualCount) {
            Location lastInvocation = finder.getLastLocation(chunk);
            reporter.tooLittleActualInvocationsInOrder(new Discrepancy(wantedCount, actualCount), wanted, lastInvocation);
        } else if (wantedCount < actualCount) {
            Location firstUndesired = chunk.get(wantedCount).getLocation();
            reporter.tooManyActualInvocationsInOrder(wantedCount, actualCount, wanted, firstUndesired);
        }
        
        invocationMarker.markVerifiedInOrder(chunk, wanted, context);
    }
}