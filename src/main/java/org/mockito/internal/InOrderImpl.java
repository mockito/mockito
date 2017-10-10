/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal;

import org.mockito.InOrder;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.InOrderWrapper;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.verification.VerificationWrapper;
import org.mockito.internal.verification.VerificationWrapperInOrderWrapper;

import static org.mockito.internal.exceptions.Reporter.inOrderRequiresFamiliarMock;

import java.util.LinkedList;
import java.util.List;

/**
 * Allows verifying in order. This class should not be exposed, hence default access.
 */
public class InOrderImpl implements InOrder, InOrderContext {

    private final MockitoCore mockitoCore = new MockitoCore();
    private final List<Object> mocksToBeVerifiedInOrder = new LinkedList<Object>();
    private final InOrderContext inOrderContext = new InOrderContextImpl();

    public List<Object> getMocksToBeVerifiedInOrder() {
        return mocksToBeVerifiedInOrder;
    }

    public InOrderImpl(List<?> mocksToBeVerifiedInOrder) {
        this.mocksToBeVerifiedInOrder.addAll(mocksToBeVerifiedInOrder);
    }

    public <T> T verify(T mock) {
        return this.verify(mock, VerificationModeFactory.times(1));
    }

    public <T> T verify(T mock, VerificationMode mode) {
        if (!mocksToBeVerifiedInOrder.contains(mock)) {
            throw inOrderRequiresFamiliarMock();
        }
        if (mode instanceof VerificationWrapper) {
            return mockitoCore.verify(mock, new VerificationWrapperInOrderWrapper((VerificationWrapper) mode, this));
        }  else if (!(mode instanceof VerificationInOrderMode)) {
            throw new MockitoException(mode.getClass().getSimpleName() + " is not implemented to work with InOrder");
        }
        return mockitoCore.verify(mock, new InOrderWrapper((VerificationInOrderMode) mode, this));
    }

    public boolean isVerified(Invocation i) {
        return inOrderContext.isVerified(i);
    }

    public void markVerified(Invocation i) {
        inOrderContext.markVerified(i);
    }

    public void verifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractionsInOrder(mocksToBeVerifiedInOrder, this);
    }
}
