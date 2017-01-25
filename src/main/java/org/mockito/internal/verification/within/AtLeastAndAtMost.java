package org.mockito.internal.verification.within;

import static org.mockito.internal.verification.within.VerificationResult.GIVE_ME_THE_NEXT_INVOCATION;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

public class AtLeastAndAtMost implements VerificationStrategy {

    private final AtLeast atLeast;
    private final AtMost atMost;

    public AtLeastAndAtMost(int minNumberOfInvocations, int maxNumberOfInvocations) {
        if (maxNumberOfInvocations<=1){
            throw new MockitoException("The maximum number of invocations must be greater than 1!");
        }
        if (minNumberOfInvocations>=maxNumberOfInvocations){
            throw new MockitoException("The minimum number of invocations must be greater than the maximum! Got: min="+minNumberOfInvocations+" max="+maxNumberOfInvocations);
        }
        
        this.atLeast = new AtLeast(minNumberOfInvocations);
        this.atMost = new AtMost(maxNumberOfInvocations);
    }

    @Override
    public VerificationResult verifyNotMatchingInvocation(Invocation invocation, MatchableInvocation wanted) {
        return GIVE_ME_THE_NEXT_INVOCATION;
    }

    @Override
    public VerificationResult verifyMatchingInvocation(Invocation invocation, MatchableInvocation wanted) {
        atLeast.verifyMatchingInvocation(invocation, wanted);
        return atMost.verifyMatchingInvocation(invocation, wanted);
    }

    @Override
    public void verifyAfterTimeElapsed(MatchableInvocation wanted) {
        atLeast.verifyAfterTimeElapsed(wanted);
        atMost.verifyAfterTimeElapsed(wanted);

    }

}
