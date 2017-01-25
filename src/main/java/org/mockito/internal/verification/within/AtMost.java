package org.mockito.internal.verification.within;

import static org.mockito.internal.exceptions.Reporter.neverWantedButInvoked;
import static org.mockito.internal.exceptions.Reporter.tooManyActualInvocations;
import static org.mockito.internal.verification.within.VerificationResult.GIVE_ME_THE_NEXT_INVOCATION;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

public class AtMost implements VerificationStrategy {

    private final int maxInvocations;
    private int matchingInvocations=0;
    private Invocation lastMatchingInvocation;

    public AtMost(int maxNumberOfInvocations) {
        if (maxNumberOfInvocations<0){
            throw new MockitoException("At most doesn't accept negative values! Got:"+maxNumberOfInvocations);
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
        lastMatchingInvocation=invocation;
        
        if (maxInvocations==0){
            throw neverWantedButInvoked(wanted, lastMatchingInvocation.getLocation());
        }
        
        return GIVE_ME_THE_NEXT_INVOCATION;
    }

    @Override
    public void verifyAfterTimeElapsed(MatchableInvocation wanted) {
        if (matchingInvocations > maxInvocations) {
            throw tooManyActualInvocations(maxInvocations, matchingInvocations, wanted, lastMatchingInvocation.getLocation());
        }
    }
}