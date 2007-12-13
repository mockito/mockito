package org.mockito.internal.invocation;

import java.util.List;

import org.mockito.internal.progress.OngoingVerifyingMode;

//TODO name
public class InvocationsMarker {

    public void markInvocationsAsVerified(List<Invocation> invocations,
            InvocationMatcher wanted, OngoingVerifyingMode mode) {
        if (mode.wantedCountIsZero()) {
            return;
        }

        if (mode.orderOfInvocationsMatters()) {
            markVerifiedInOrder(invocations);
        } else {
            markVerified(wanted, invocations);
        }
    }

    private void markVerified(InvocationMatcher expected, List<Invocation> invocations) {
        for (Invocation i : invocations) {
            if (expected.matches(i)) {
                i.markVerified();
            }
        }
    }

    private void markVerifiedInOrder(List<Invocation> invocations) {
        for (Invocation i : invocations) {
            i.markVerifiedInOrder();
        }
    }

}
