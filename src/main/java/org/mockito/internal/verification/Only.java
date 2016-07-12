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

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class Only implements VerificationMode {

    @SuppressWarnings("unchecked")
    public void verify(VerificationData data) {
        InvocationMatcher wantedMatcher = data.getWanted();
        List<Invocation> invocations = data.getAllInvocations();
        List<Invocation> chunk = findInvocations(invocations,wantedMatcher);
        if (invocations.size() != 1 && chunk.size() > 0) {            
            Invocation unverified = findFirstUnverified(invocations);
            throw noMoreInteractionsWanted(unverified, (List) invocations);
        } 
        if (invocations.size() != 1 || chunk.size() == 0) {
            throw wantedButNotInvoked(wantedMatcher);
        }
        markVerified(chunk.get(0), wantedMatcher);
    }

    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
