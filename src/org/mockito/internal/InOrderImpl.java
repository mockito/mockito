/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.LinkedList;
import java.util.List;

import org.mockito.InOrder;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.InOrderWrapper;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.verification.VerificationMode;

/**
 * Allows verifying in order. This class should not be exposed, hence default access.
 */
class InOrderImpl implements InOrder {
    
    private final MockitoCore mockitoCore = new MockitoCore();
    private final Reporter reporter = new Reporter();
    private final List<Object> mocksToBeVerifiedInOrder = new LinkedList<Object>();
    
    public InOrderImpl(List<Object> mocksToBeVerifiedInOrder) {
        this.mocksToBeVerifiedInOrder.addAll(mocksToBeVerifiedInOrder);
    }

    public <T> T verify(T mock) {
        return this.verify(mock, VerificationModeFactory.times(1));
    }
    
    public <T> T verify(T mock, VerificationMode mode) {
        if (!mocksToBeVerifiedInOrder.contains(mock)) {
            reporter.inOrderRequiresFamiliarMock();
        } else if (!(mode instanceof VerificationInOrderMode)) {
            throw new MockitoException(mode.getClass().getSimpleName() + " is not implemented to work with InOrder");
        }
        return mockitoCore.verify(mock, new InOrderWrapper((VerificationInOrderMode) mode, mocksToBeVerifiedInOrder));
    }
}