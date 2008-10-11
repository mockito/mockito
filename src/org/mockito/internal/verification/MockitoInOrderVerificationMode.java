package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.MockitoVerificationMode.Verification;
import org.mockito.verification.VerificationMode;

public class MockitoInOrderVerificationMode implements VerificationMode {

    private final MockitoVerificationMode mode;
    private final List<Object> mocksToBeVerifiedInOrder;

    public MockitoInOrderVerificationMode(MockitoVerificationMode mode, List<Object> mocksToBeVerifiedInOrder) {
        this.mode = mode;
        this.mocksToBeVerifiedInOrder = mocksToBeVerifiedInOrder;
    }

    @Override
    //TODO this API is not valid here - we want ALL invocations here
    public void verify(List<Invocation> invocations, InvocationMatcher wanted) {
        List<Invocation> allInvocations;
        allInvocations = new AllInvocationsFinder().getAllInvocations(mocksToBeVerifiedInOrder);
        
        MissingInvocationInOrderVerifier missingInvocation = new MissingInvocationInOrderVerifier();
        NumberOfInvocationsInOrderVerifier numberOfCalls = new NumberOfInvocationsInOrderVerifier();
        
        if (mode.wantedCount() > 0 || (mode.verification == Verification.AT_LEAST && mode.wantedCount() == 1)) {
            missingInvocation.verify(allInvocations, wanted, this);
        }
        
        numberOfCalls.verify(allInvocations, wanted, mode);
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: " + mode.wantedCount() + ", Mocks to verify in order: " + mocksToBeVerifiedInOrder;
    }
}