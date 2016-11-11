/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.Iterator;
import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocations;
import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocationsInOrder;
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
import org.mockito.invocation.MatchableInvocation;

public class AtLeastXNumberOfInvocationsChecker {
    
    public static void checkAtLeastNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            Location lastLocation = getLastLocation(actualInvocations);
            throw tooLittleActualInvocations(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);
        }

        removeAlreadyVerified(actualInvocations);
        markVerified(actualInvocations, wanted);
    }

    private static void removeAlreadyVerified(List<Invocation> invocations) {
        for (Iterator<Invocation> iterator = invocations.iterator(); iterator.hasNext(); ) {
            Invocation i = iterator.next();
            if (i.isVerified()) {
                iterator.remove();
            }
        }
    }

    public static void checkAtLeastNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount,InOrderContext orderingContext) {
        List<Invocation> chunk = findAllMatchingUnverifiedChunks(invocations, wanted, orderingContext);

        int actualCount = chunk.size();

        if (wantedCount > actualCount) {
            Location lastLocation = getLastLocation(chunk);
            throw tooLittleActualInvocationsInOrder(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);
        }

        markVerifiedInOrder(chunk, wanted, orderingContext);
    }
}