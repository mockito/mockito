/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class AtMost implements VerificationMode {

    private final int maxNumberOfInvocations;
    private final InvocationMarker invocationMarker = new InvocationMarker();

    public AtMost(int maxNumberOfInvocations) {
        if (maxNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.maxNumberOfInvocations = maxNumberOfInvocations;
    }

    public void verify(VerificationData data) {
        List<Invocation> invocations = data.getAllInvocations();
        InvocationMatcher wanted = data.getWanted();
        
        InvocationsFinder finder = new InvocationsFinder();
        List<Invocation> found = finder.findInvocations(invocations, wanted);
        int foundSize = found.size();
        if (foundSize > maxNumberOfInvocations) {
            new Reporter().wantedAtMostX(maxNumberOfInvocations, foundSize);
        }
        
        invocationMarker.markVerified(found, wanted);
    }
}