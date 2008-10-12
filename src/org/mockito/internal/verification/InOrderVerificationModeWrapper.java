package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.internal.verification.api.VerificationMode;

public class InOrderVerificationModeWrapper implements VerificationMode {

    private final VerificationInOrderMode mode;
    private final List<Object> mocksToBeVerifiedInOrder;

    public InOrderVerificationModeWrapper(VerificationInOrderMode mode, List<Object> mocksToBeVerifiedInOrder) {
        this.mode = mode;
        this.mocksToBeVerifiedInOrder = mocksToBeVerifiedInOrder;
    }

    public void verify(VerificationData data) {
        List<Invocation> allInvocations = new AllInvocationsFinder().getAllInvocations(mocksToBeVerifiedInOrder);
        mode.verifyInOrder(new VerificationDataImpl(allInvocations, data.getWanted()));
    }
}
