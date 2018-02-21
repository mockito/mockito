/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.List;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocations;
import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocationsInOrder;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationMarker.markVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findAllMatchingUnverifiedChunks;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.getAllLocations;

public class AtLeastXNumberOfInvocationsChecker {

    public static void checkAtLeastNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);

        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            List<Location> allLocations = getAllLocations(actualInvocations);
            throw tooLittleActualInvocations(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, allLocations);
        }

        markVerified(actualInvocations, wanted);
    }

    public static void checkAtLeastNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount,InOrderContext orderingContext) {
        List<Invocation> chunk = findAllMatchingUnverifiedChunks(invocations, wanted, orderingContext);

        int actualCount = chunk.size();

        if (wantedCount > actualCount) {
            List<Location> allLocations = getAllLocations(chunk);
            throw tooLittleActualInvocationsInOrder(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, allLocations);
        }

        markVerifiedInOrder(chunk, wanted, orderingContext);
    }
}
