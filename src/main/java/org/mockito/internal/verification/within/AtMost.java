/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.within;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

import static java.util.Collections.singletonList;
import static org.mockito.internal.exceptions.Reporter.tooManyActualInvocations;
import static org.mockito.internal.verification.within.VerificationResult.GIVE_ME_THE_NEXT_INVOCATION;

public class AtMost implements VerificationStrategy {

    private final int maxInvocations;
    private int matchingInvocations = 0;
    private Invocation lastMatchingInvocation;

    public AtMost(int maxNumberOfInvocations) {
        if (maxNumberOfInvocations < 0) {
            throw new MockitoException("At most doesn't accept negative values! Got:" + maxNumberOfInvocations);
        }
        this.maxInvocations = maxNumberOfInvocations;
    }

    @Override
    public VerificationResult verifyNotMatchingInvocation(Invocation invocation, MatchableInvocation wanted) {
        return GIVE_ME_THE_NEXT_INVOCATION;
    }

    @Override
    public VerificationResult verifyMatchingInvocation(Invocation invocation, MatchableInvocation wanted) {
        matchingInvocations++;
        lastMatchingInvocation = invocation;

        //we can fail early if we have more invocations than allowed
        //no point to wait until whole time elapsed
        //calling below fuction will do that:
        verifyAfterTimeElapsed(wanted);

        return GIVE_ME_THE_NEXT_INVOCATION;
    }

    @Override
    public void verifyAfterTimeElapsed(MatchableInvocation wanted) {
        if (matchingInvocations > maxInvocations) {
            throw tooManyActualInvocations(maxInvocations, matchingInvocations, wanted, singletonList(lastMatchingInvocation.getLocation()));
        }
    }
}
