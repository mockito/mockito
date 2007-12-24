/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.ActualInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeImpl;

/**
 * Marks invocations as verified / verified strictly
 */
public class MarkingVerifier implements Verifier {
    
    private final ActualInvocationsFinder finder;

    public MarkingVerifier() {
        this(new ActualInvocationsFinder());
    }
    
    MarkingVerifier(ActualInvocationsFinder finder) {
        this.finder = finder;
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.explicitMode()) {
            return;
        }
        
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted, mode);
        
        for (Invocation invocation : actualInvocations) {
            invocation.markVerified();
            if (mode.strictMode()) {
                invocation.markVerifiedStrictly();
            }
        }
    }
}
