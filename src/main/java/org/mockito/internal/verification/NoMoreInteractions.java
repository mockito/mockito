/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import static org.mockito.exceptions.Reporter.noMoreInteractionsWanted;
import static org.mockito.exceptions.Reporter.noMoreInteractionsWantedInOrder;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class NoMoreInteractions implements VerificationMode, VerificationInOrderMode {

    @SuppressWarnings("unchecked")
    public void verify(VerificationData data) {
        Invocation unverified = new InvocationsFinder().findFirstUnverified(data.getAllInvocations());
        if (unverified != null) {
            throw noMoreInteractionsWanted(unverified, (List) data.getAllInvocations());
        }
    }

    public void verifyInOrder(VerificationDataInOrder data) {
        List<Invocation> invocations = data.getAllInvocations();
        Invocation unverified = new InvocationsFinder().findFirstUnverifiedInOrder(data.getOrderingContext(), invocations);
        
        if (unverified != null) {
            throw noMoreInteractionsWantedInOrder(unverified);
        }
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}