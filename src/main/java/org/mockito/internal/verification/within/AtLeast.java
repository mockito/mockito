package org.mockito.internal.verification.within;

import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocations;
import static org.mockito.internal.verification.within.VerificationResult.GIVE_ME_THE_NEXT_INVOCATION;
import static org.mockito.internal.verification.within.VerificationResult.FINISHED_SUCCESSFULL;

import org.mockito.internal.verification.checkers.AtLeastDiscrepancy;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

public class AtLeast implements VerificationStrategy {

    private final int minInvocations;

    private int actualInvocations;

    private Invocation lastMatchingInvocation;

    public AtLeast(int minInvocations) {
        this.minInvocations = minInvocations;
    }

    @Override
    public VerificationResult verifyNotMatchingInvocation(Invocation notMatchingInvocation, MatchableInvocation wanted) {
        return GIVE_ME_THE_NEXT_INVOCATION;
    }

    @Override
    public VerificationResult verifyMatchingInvocation(Invocation matchingInvocation, MatchableInvocation wanted) {
        actualInvocations++;

        if (actualInvocations >= minInvocations) {
            return FINISHED_SUCCESSFULL;
        }

        lastMatchingInvocation = matchingInvocation;

        return GIVE_ME_THE_NEXT_INVOCATION;
    }

    @Override
    public void verifyAfterTimeElapsed(MatchableInvocation wanted) {
        if (actualInvocations >= minInvocations) {
            return;
        }
        AtLeastDiscrepancy discrepancy = new AtLeastDiscrepancy(minInvocations, actualInvocations);
        Location location;
        if (actualInvocations == 0) {
            location = null;
        }else{
            location=lastMatchingInvocation.getLocation();
        }

        throw tooLittleActualInvocations(discrepancy, wanted, location);
    }
}