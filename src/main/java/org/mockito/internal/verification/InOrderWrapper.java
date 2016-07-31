/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.InOrderImpl;
import org.mockito.internal.invocation.finder.VerifiableInvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

import java.util.List;

public class InOrderWrapper implements VerificationMode {

    private final VerificationInOrderMode mode;
    private final InOrderImpl inOrder;

    public InOrderWrapper(VerificationInOrderMode mode, InOrderImpl inOrder) {
        this.mode = mode;
        this.inOrder = inOrder;        
    }

    public void verify(VerificationData data) {
        List<Invocation> invocations = VerifiableInvocationsFinder.find(inOrder.getMocksToBeVerifiedInOrder());
        VerificationDataInOrderImpl dataInOrder = new VerificationDataInOrderImpl(inOrder, invocations, data.getWanted());
        mode.verifyInOrder(dataInOrder);
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}