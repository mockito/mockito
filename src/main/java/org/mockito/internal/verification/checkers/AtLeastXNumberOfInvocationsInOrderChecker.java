/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocationsInOrder;
import static org.mockito.internal.invocation.InvocationMarker.markVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findAllMatchingUnverifiedChunks;
import static org.mockito.internal.invocation.InvocationsFinder.getLastLocation;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class AtLeastXNumberOfInvocationsInOrderChecker {
    
    private final InOrderContext orderingContext;
    
    public AtLeastXNumberOfInvocationsInOrderChecker(InOrderContext orderingContext) {
        this.orderingContext = orderingContext;
    }

    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> chunk = findAllMatchingUnverifiedChunks(invocations, wanted, orderingContext);
        
        int actualCount = chunk.size();
        
        if (wantedCount > actualCount) {
            Location lastLocation = getLastLocation(chunk);
            throw tooLittleActualInvocationsInOrder(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);
        }
        
        markVerifiedInOrder(chunk, wanted, orderingContext);
    }
}
