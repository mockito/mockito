/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.List;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

import static org.mockito.internal.exceptions.Reporter.neverWantedButInvoked;
import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocations;
import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocationsInOrder;
import static org.mockito.internal.exceptions.Reporter.tooManyActualInvocations;
import static org.mockito.internal.exceptions.Reporter.tooManyActualInvocationsInOrder;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationMarker.markVerifiedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findFirstMatchingUnverifiedInvocation;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.findMatchingChunk;
import static org.mockito.internal.invocation.InvocationsFinder.getLastLocation;

public class NumberOfInvocationsChecker {

    private NumberOfInvocationsChecker() {
    }

    public static void checkNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);

        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            Location lastInvocation = getLastLocation(actualInvocations);
            throw tooLittleActualInvocations(new Discrepancy(wantedCount, actualCount), wanted, lastInvocation);
        }
        if (wantedCount == 0 && actualCount > 0) {
            Location firstUndesired = actualInvocations.get(wantedCount).getLocation();
            throw neverWantedButInvoked(wanted, firstUndesired);
        }
        if (wantedCount < actualCount) {
            Location firstUndesired = actualInvocations.get(wantedCount).getLocation();
            throw tooManyActualInvocations(wantedCount, actualCount, wanted, firstUndesired);
        }

        markVerified(actualInvocations, wanted);
    }

    public static void checkNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount, InOrderContext context) {
        List<Invocation> chunk = findMatchingChunk(invocations, wanted, wantedCount, context);

        int actualCount = chunk.size();

        if (wantedCount > actualCount) {
            Location lastInvocation = getLastLocation(chunk);
            throw tooLittleActualInvocationsInOrder(new Discrepancy(wantedCount, actualCount), wanted, lastInvocation);
        }
        if (wantedCount < actualCount) {
            Location firstUndesired = chunk.get(wantedCount).getLocation();
            throw tooManyActualInvocationsInOrder(wantedCount, actualCount, wanted, firstUndesired);
        }

        markVerifiedInOrder(chunk, wanted, context);
    }

    public static void checkNumberOfInvocationsNonGreedy(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount, InOrderContext context) {
        int actualCount = 0;
        Location lastLocation = null;
        while( actualCount < wantedCount ){
            Invocation next = findFirstMatchingUnverifiedInvocation(invocations, wanted, context );
            if( next == null ){
                throw tooLittleActualInvocationsInOrder(new Discrepancy(wantedCount, actualCount), wanted, lastLocation );
            }
            markVerified( next, wanted );
            context.markVerified( next );
            lastLocation = next.getLocation();
            actualCount++;
        }
    }
}
