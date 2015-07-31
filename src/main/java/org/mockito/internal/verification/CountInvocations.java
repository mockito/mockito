package org.mockito.internal.verification;

import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tim on 31-7-15.
 */
public class CountInvocations implements VerificationMode {

    private AtomicInteger invocationsCounter;

    public CountInvocations(AtomicInteger invocationsCounter) {
        this.invocationsCounter = invocationsCounter;
    }

    @Override
    public void verify(VerificationData data) {
        InvocationsFinder finder = new InvocationsFinder();
        List<Invocation> found = finder.findInvocations(data.getAllInvocations(), data.getWanted());
        invocationsCounter.set(found.size());
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
