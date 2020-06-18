/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.internal.exceptions.Reporter.noMoreInteractionsWanted;
import static org.mockito.internal.exceptions.Reporter.wantedButNotInvoked;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationsFinder.findFirstUnverified;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;

import java.util.List;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.verification.VerificationMode;

public class Only implements VerificationMode {

    @SuppressWarnings("unchecked")
    public void verify(VerificationData data) {
        MatchableInvocation target = data.getTarget();
        List<Invocation> invocations = data.getAllInvocations();
        List<Invocation> chunk = findInvocations(invocations, target);
        if (invocations.size() != 1 && !chunk.isEmpty()) {
            Invocation unverified = findFirstUnverified(invocations);
            throw noMoreInteractionsWanted(unverified, (List) invocations);
        }
        if (invocations.size() != 1 || chunk.isEmpty()) {
            throw wantedButNotInvoked(target);
        }
        markVerified(chunk.get(0), target);
    }
}
