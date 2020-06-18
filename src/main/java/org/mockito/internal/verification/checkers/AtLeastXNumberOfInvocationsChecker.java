/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static org.mockito.internal.exceptions.Reporter.tooFewActualInvocations;
import static org.mockito.internal.exceptions.Reporter.tooFewActualInvocationsInOrder;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationMarker.markVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findAllMatchingUnverifiedChunks;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.getAllLocations;

import java.util.List;

import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

public class AtLeastXNumberOfInvocationsChecker {

    public static void checkAtLeastNumberOfInvocations(
            List<Invocation> invocations, MatchableInvocation wanted, int wantedCount) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);

        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            List<Location> allLocations = getAllLocations(actualInvocations);
            throw tooFewActualInvocations(
                    new AtLeastDiscrepancy(wantedCount, actualCount), wanted, allLocations);
        }

        markVerified(actualInvocations, wanted);
    }

    public static void checkAtLeastNumberOfInvocations(
            List<Invocation> invocations,
            MatchableInvocation wanted,
            int wantedCount,
            InOrderContext orderingContext) {
        List<Invocation> chunk =
                findAllMatchingUnverifiedChunks(invocations, wanted, orderingContext);

        int actualCount = chunk.size();

        if (wantedCount > actualCount) {
            List<Location> allLocations = getAllLocations(chunk);
            throw tooFewActualInvocationsInOrder(
                    new AtLeastDiscrepancy(wantedCount, actualCount), wanted, allLocations);
        }

        markVerifiedInOrder(chunk, wanted, orderingContext);
    }
}
