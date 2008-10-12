/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.verification.VerificationData;
import org.mockito.verification.VerificationMode;

public class AtMostXVerificationMode implements VerificationMode {

    private final int maxNumberOfInvocations;

    public AtMostXVerificationMode(int maxNumberOfInvocations) {
        if (maxNumberOfInvocations < 0) {
            throw new MockitoException("maxNumberOfInvocations cannot be negative");
        }
        this.maxNumberOfInvocations = maxNumberOfInvocations;
    }

    @Override
    public void verify(VerificationData data) {
        List<Invocation> invocations = data.getAllInvocations();
        InvocationMatcher wanted = data.getWanted();
        
        InvocationsFinder finder = new InvocationsFinder();
        List<Invocation> found = finder.findInvocations(invocations, wanted);
        int foundSize = found.size();
        if (foundSize > maxNumberOfInvocations) {
            throw new MockitoAssertionError("Wanted at most " + maxNumberOfInvocations + " but found: " + foundSize);
        }
    }
}