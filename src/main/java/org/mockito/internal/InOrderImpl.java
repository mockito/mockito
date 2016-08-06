/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal;

import static org.mockito.internal.exceptions.Reporter.inOrderRequiresFamiliarMock;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.util.LinkedList;
import java.util.List;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.InOrderWrapper;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationWrapper;
import org.mockito.verification.VerificationWrapperInOrderWrapper;

/**
 * Allows verifying in order. This class should not be exposed, hence default access.
 */
public class InOrderImpl implements InOrder, InOrderContext {
    
    private final List<Object> mocksToBeVerifiedInOrder = new LinkedList<Object>();
    private final InOrderContext inOrderContext = new InOrderContextImpl();
    
    public List<Object> getMocksToBeVerifiedInOrder() {
        return mocksToBeVerifiedInOrder;
    }

    public InOrderImpl(List<? extends Object> mocksToBeVerifiedInOrder) {
        this.mocksToBeVerifiedInOrder.addAll(mocksToBeVerifiedInOrder);
    }

    @Override
    public <T> T verify(T mock) {
        return verify(mock, times(1));
    }
    
    @Override
    public <T> T verify(T mock, VerificationMode mode) {
        if (!mocksToBeVerifiedInOrder.contains(mock)) {
            throw inOrderRequiresFamiliarMock();
        }
        if (mode instanceof VerificationWrapper) {
            return Mockito.verify(mock, new VerificationWrapperInOrderWrapper((VerificationWrapper) mode, this));
        }  else if (!(mode instanceof VerificationInOrderMode)) {
            throw new MockitoException(mode.getClass().getSimpleName() + " is not implemented to work with InOrder");
        }
        return Mockito.verify(mock, new InOrderWrapper((VerificationInOrderMode) mode, this));
    }

    @Override
    public boolean isVerified(Invocation i) {
        return inOrderContext.isVerified(i);
    }

    @Override
    public void markVerified(Invocation i) {
        inOrderContext.markVerified(i);
    }

    @Override
    public void verifyNoMoreInteractions() {
        MockitoCore.verifyNoMoreInteractionsInOrder(mocksToBeVerifiedInOrder, this);
    }
}
