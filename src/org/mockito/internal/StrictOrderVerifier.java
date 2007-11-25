/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.*;

public class StrictOrderVerifier implements Strictly {
    
    List<Object> mocksToBeVerifiedInOrder = new LinkedList<Object>();
    
    public <T> T verify(T mock) {
        return this.verify(mock, 1);
    }
    
    //TODO get rid of interface with int
    public <T> T verify(T mock, int expectedNumberOfInvocations) {
        return Mockito.verify(mock, VerifyingMode.inOrder(expectedNumberOfInvocations, mocksToBeVerifiedInOrder));
    }

    public void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(mocksToBeVerifiedInOrder.toArray());
    }

    public void addMockToBeVerifiedInOrder(Object mock) {
        mocksToBeVerifiedInOrder.add(mock);
    }
}
