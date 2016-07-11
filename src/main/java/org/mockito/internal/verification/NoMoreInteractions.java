/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import static org.mockito.internal.exceptions.Reporter.noMoreInteractionsWanted;
import static org.mockito.internal.exceptions.Reporter.noMoreInteractionsWantedInOrder;
import static org.mockito.internal.invocation.InvocationsFinder.findFirstUnverified;
import static org.mockito.internal.invocation.InvocationsFinder.findFirstUnverifiedInOrder;

import java.util.List;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class NoMoreInteractions implements VerificationMode, VerificationInOrderMode {

    @SuppressWarnings("unchecked")
    public void verify(VerificationData data) {
        Invocation unverified = findFirstUnverified(data.getAllInvocations());
        if (unverified != null) {
            throw noMoreInteractionsWanted(unverified, (List) data.getAllInvocations());
        }
    }

    public void verifyInOrder(VerificationDataInOrder data) {
        List<Invocation> invocations = data.getAllInvocations();
        Invocation unverified = findFirstUnverifiedInOrder(data.getOrderingContext(), invocations);
        
        if (unverified != null) {
            throw noMoreInteractionsWantedInOrder(unverified);
        }
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
