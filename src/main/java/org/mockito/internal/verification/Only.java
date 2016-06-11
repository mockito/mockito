/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.exceptions.Reporter.noMoreInteractionsWanted;
import static org.mockito.exceptions.Reporter.wantedButNotInvoked;

import java.util.List;

import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class Only implements VerificationMode {

    private final InvocationsFinder finder = new InvocationsFinder();
    private final InvocationMarker marker = new InvocationMarker();

    @SuppressWarnings("unchecked")
    public void verify(VerificationData data) {
        InvocationMatcher wantedMatcher = data.getWanted();
        List<Invocation> invocations = data.getAllInvocations();
        List<Invocation> chunk = finder.findInvocations(invocations,wantedMatcher);
        if (invocations.size() != 1 && chunk.size() > 0) {            
            Invocation unverified = finder.findFirstUnverified(invocations);
            throw noMoreInteractionsWanted(unverified, (List) invocations);
        } 
        if (invocations.size() != 1 || chunk.size() == 0) {
            throw wantedButNotInvoked(wantedMatcher);
        }
        marker.markVerified(chunk.get(0), wantedMatcher);
    }

    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
