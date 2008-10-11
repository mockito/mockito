/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.verification.MockitoInOrderVerificationMode;
import org.mockito.internal.verification.MockitoVerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.verification.VerificationMode;

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
        }
        if (mode instanceof MockitoVerificationMode) {
            return Mockito.verify(mock, new MockitoInOrderVerificationMode((MockitoVerificationMode) mode, mocksToBeVerifiedInOrder));
        }
        throw new RuntimeException("nothing else is yet implemented");
//        if (!(mode instanceof InOrderVerificationMode) {
//            //TODO fail nicely here
//        }
//        return Mockito.verify(mock, mode);
    }
}