/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.InOrderVerificationModeWrapper;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.internal.verification.api.VerificationMode;

/**
 * Allows verifying in order. This class should not be exposed, hence default access.
 */
class InOrderVerifier implements InOrder {
    
    private final Reporter reporter = new Reporter();
    private final List<Object> mocksToBeVerifiedInOrder = new LinkedList<Object>();
    
    public InOrderVerifier(List<Object> mocksToBeVerifiedInOrder) {
        this.mocksToBeVerifiedInOrder.addAll(mocksToBeVerifiedInOrder);
    }

    public <T> T verify(T mock) {
        return this.verify(mock, VerificationModeFactory.times(1));
    }
    
    public <T> T verify(T mock, VerificationMode mode) {
        if (!mocksToBeVerifiedInOrder.contains(mock)) {
            reporter.inOrderRequiresFamiliarMock();
        } else if (!(mode instanceof VerificationInOrderMode)) {
            throw new MockitoException("VerificationMode is not implmented to work with InOrder");
        }
        return Mockito.verify(mock, new InOrderVerificationModeWrapper((VerificationInOrderMode) mode, mocksToBeVerifiedInOrder));
    }
}