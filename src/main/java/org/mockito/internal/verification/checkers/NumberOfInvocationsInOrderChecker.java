/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.exceptions.Reporter.tooLittleActualInvocationsInOrder;
import static org.mockito.exceptions.Reporter.tooManyActualInvocationsInOrder;
import static org.mockito.internal.invocation.InvocationMarker.markVerifiedInOrder;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class NumberOfInvocationsInOrderChecker {
  
    private final InvocationsFinder finder;
    
    public NumberOfInvocationsInOrderChecker() {
        this(new InvocationsFinder());
    }
    
    NumberOfInvocationsInOrderChecker(InvocationsFinder finder) {
        this.finder = finder;
    }
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount, InOrderContext context) {
        List<Invocation> chunk = finder.findMatchingChunk(invocations, wanted, wantedCount, context);
        
        int actualCount = chunk.size();
        
        if (wantedCount > actualCount) {
            Location lastInvocation = finder.getLastLocation(chunk);
            throw tooLittleActualInvocationsInOrder(new Discrepancy(wantedCount, actualCount), wanted, lastInvocation);
        } 
        if (wantedCount < actualCount) {
            Location firstUndesired = chunk.get(wantedCount).getLocation();
            throw tooManyActualInvocationsInOrder(wantedCount, actualCount, wanted, firstUndesired);
        }
        
        markVerifiedInOrder(chunk, wanted, context);
    }
}