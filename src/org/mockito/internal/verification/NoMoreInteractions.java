/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("rawtypes")
public class NoMoreInteractions implements VerificationMode, VerificationInOrderMode {

    @SuppressWarnings("unchecked")
    public void verify(final VerificationData data) {
        final Invocation unverified = new InvocationsFinder().findFirstUnverified(data.getAllInvocations());
        if (unverified != null) {
            new Reporter().noMoreInteractionsWanted(unverified, (List) data.getAllInvocations());
        }
    }

    public void verifyInOrder(final VerificationDataInOrder data) {
        final List<Invocation> invocations = data.getAllInvocations();
        final Invocation unverified = new InvocationsFinder().findFirstUnverifiedInOrder(data.getOrderingContext(), invocations);
        
        if (unverified != null) {
            new Reporter().noMoreInteractionsWantedInOrder(unverified);
        }
    }
}