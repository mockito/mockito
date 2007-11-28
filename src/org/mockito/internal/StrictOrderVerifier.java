/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.*;
import org.mockito.exceptions.Exceptions;

public class StrictOrderVerifier implements Strictly {
    
    List<Object> mocksToBeVerifiedInOrder = new LinkedList<Object>();
    
    public <T> T verify(T mock) {
        return this.verify(mock, 1);
    }
    //TODO get rid of interface with int
    public <T> T verify(T mock, int expectedNumberOfInvocations) {
        return this.verify(mock, VerifyingMode.inOrder(expectedNumberOfInvocations, mocksToBeVerifiedInOrder));
    }
    
    public <T> T verify(T mock, VerifyingMode verifyingMode) {
        if (!mocksToBeVerifiedInOrder.contains(mock)) {
            Exceptions.strictlyRequiresFamiliarMock();
        }
        return Mockito.verify(mock, VerifyingMode.inOrder(verifyingMode.expectedCount(), mocksToBeVerifiedInOrder));
    }

    public void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(mocksToBeVerifiedInOrder.toArray());
    }

    public void addMockToBeVerifiedInOrder(Object mock) {
        mocksToBeVerifiedInOrder.add(mock);
    }
}
