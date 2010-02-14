/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class NoMoreInteractions implements VerificationMode {

    public void verify(VerificationData data) {
        Invocation unverified = new InvocationsFinder().findFirstUnverified(data.getAllInvocations());
        
        if (unverified != null) {
            new Reporter().noMoreInteractionsWanted(unverified);
        }
    }
}