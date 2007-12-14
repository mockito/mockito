/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.List;

import org.mockito.internal.progress.VerificationMode;

//TODO name
public class InvocationsMarker {

    public void markInvocationsAsVerified(List<Invocation> invocations,
            InvocationMatcher wanted, VerificationMode mode) {
        if (mode.wantedCountIsZero()) {
            return;
        }

        if (mode.isStrict()) {
            markVerifiedStrictly(invocations);
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

    private void markVerifiedStrictly(List<Invocation> invocations) {
        for (Invocation i : invocations) {
            i.markVerifiedStrictly();
        }
    }
}
