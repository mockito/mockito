/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.within;

import org.mockito.internal.reporting.Discrepancy;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.internal.exceptions.Reporter.*;
import static org.mockito.internal.invocation.InvocationsFinder.getLastLocation;
import static org.mockito.internal.verification.within.VerificationResult.GIVE_ME_THE_NEXT_INVOCATION;

public class Times implements VerificationStrategy {

    private final int expectedInvocations;

    private List<Invocation> matchingInvocations = new LinkedList<Invocation>();

    public Times(int exactNumberOfInvocations) {
        this.expectedInvocations = exactNumberOfInvocations;
    }

    @Override
    public VerificationResult verifyNotMatchingInvocation(Invocation invocation, MatchableInvocation wanted) {
        return GIVE_ME_THE_NEXT_INVOCATION;
    }

    @Override
    public VerificationResult verifyMatchingInvocation(Invocation invocation, MatchableInvocation wanted) {
        matchingInvocations.add(invocation);
        if (expectedInvocations == 0) {
            throw neverWantedButInvoked(wanted, singletonList(invocation.getLocation()));
        }
        if (matchingInvocations.size() > expectedInvocations) {
            throw tooManyActualInvocations(expectedInvocations, matchingInvocations.size(), wanted, singletonList(invocation.getLocation()));
        }

        return GIVE_ME_THE_NEXT_INVOCATION;
    }

    @Override
    public void verifyAfterTimeElapsed(MatchableInvocation wanted) {
        Location lastInvocation = getLastLocation(matchingInvocations);

        if (expectedInvocations > 0 && matchingInvocations.isEmpty()) {
            throw wantedButNotInvoked(wanted);
        }

        if (expectedInvocations > matchingInvocations.size()) {
            throw tooLittleActualInvocations(new Discrepancy(expectedInvocations, matchingInvocations.size()), wanted, singletonList(lastInvocation));
        }
    }

    @Override
    public String toString() {
        return "Times [expectedInvocations=" + expectedInvocations + ", matchingInvocations=" + matchingInvocations + "]";
    }
}
