package org.mockito.internal.verification.within;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

public interface VerificationStrategy {

    /**
     * Will be called if an invocation was detected on a mock that doesn't match the wanted invocation
     * @param invocation the invocation that doesn't match 
     * @param wanted the expected invocation
     * @return 
     */
    VerificationResult verifyNotMatchingInvocation(Invocation invocation, MatchableInvocation wanted);

    VerificationResult verifyMatchingInvocation(Invocation invocation, MatchableInvocation wanted);

    void verifyAfterTimeElapsed(MatchableInvocation wanted);
}