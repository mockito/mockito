/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.internal.exceptions.Reporter.wantedAtMostX;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;

import java.util.Iterator;
import java.util.List;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.verification.VerificationMode;

public class AtMost implements VerificationMode {

    private final int maxNumberOfInvocations;

    public AtMost(int maxNumberOfInvocations) {
        if (maxNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.maxNumberOfInvocations = maxNumberOfInvocations;
    }

    public void verify(VerificationData data) {
        List<Invocation> invocations = data.getAllInvocations();
        MatchableInvocation wanted = data.getTarget();

        List<Invocation> found = findInvocations(invocations, wanted);
        int foundSize = found.size();
        if (foundSize > maxNumberOfInvocations) {
            throw wantedAtMostX(maxNumberOfInvocations, foundSize);
        }

        removeAlreadyVerified(found);
        markVerified(found, wanted);
    }

    private void removeAlreadyVerified(List<Invocation> invocations) {
        for (Iterator<Invocation> iterator = invocations.iterator(); iterator.hasNext(); ) {
            Invocation i = iterator.next();
            if (i.isVerified()) {
                iterator.remove();
            }
        }
    }
}
