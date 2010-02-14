/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.verification.VerificationMode;

public class InOrderWrapper implements VerificationMode {

    private final VerificationInOrderMode mode;
    private final List<Object> mocksToBeVerifiedInOrder;

    public InOrderWrapper(VerificationInOrderMode mode, List<Object> mocksToBeVerifiedInOrder) {
        this.mode = mode;
        this.mocksToBeVerifiedInOrder = mocksToBeVerifiedInOrder;
    }

    public void verify(VerificationData data) {
        List<Invocation> allInvocations = new AllInvocationsFinder().find(mocksToBeVerifiedInOrder);
        mode.verifyInOrder(new VerificationDataImpl(allInvocations, data.getWanted()));
    }
}
