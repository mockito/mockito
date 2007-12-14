/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.*;

import org.mockito.exceptions.Exceptions;
import org.mockito.internal.progress.VerificationMode;

class StrictOrderVerifier implements Strictly {
    
    List<Object> mocksToBeVerifiedSrictly = new LinkedList<Object>();
    
    public <T> T verify(T mock) {
        return this.verify(mock, 1);
    }
    //TODO VerificationMode should be interfaced so that 'ongoing' bit is hidden
    //TODO get rid of interface with int
    public <T> T verify(T mock, int wantedNumberOfInvocations) {
        return this.verify(mock, VerificationMode.strict(wantedNumberOfInvocations, mocksToBeVerifiedSrictly));
    }
    
    public <T> T verify(T mock, VerificationMode verificationMode) {
        if (!mocksToBeVerifiedSrictly.contains(mock)) {
            Exceptions.strictlyRequiresFamiliarMock();
        }
        return Mockito.verify(mock, VerificationMode.strict(verificationMode.wantedCount(), mocksToBeVerifiedSrictly));
    }

    public void addMockToBeVerifiedStrictly(Object mock) {
        mocksToBeVerifiedSrictly.add(mock);
    }
}
