/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.exceptions.Reporter.tooLittleActualInvocations;
import static org.mockito.exceptions.Reporter.tooLittleActualInvocationsInOrder;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationMarker.markVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findAllMatchingUnverifiedChunks;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.getLastLocation;

import java.util.List;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class AtLeastXNumberOfInvocationsChecker {
    
    private AtLeastXNumberOfInvocationsChecker() {
    }
    public static void checkAtLeastNumberOfInvocations(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            Location lastLocation = getLastLocation(actualInvocations);
            throw tooLittleActualInvocations(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);        
        }
        
        markVerified(actualInvocations, wanted);
    }
    
    public static void checkAtLeastNumberOfInvocations(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount, InOrderContext orderingContext) {
        List<Invocation> chunk = findAllMatchingUnverifiedChunks(invocations, wanted, orderingContext);
        
        int actualCount = chunk.size();
        
        if (wantedCount > actualCount) {
            Location lastLocation = getLastLocation(chunk);
            throw tooLittleActualInvocationsInOrder(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);
        }
        
        markVerifiedInOrder(chunk, wanted, orderingContext);
    }
}