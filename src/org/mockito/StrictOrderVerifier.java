/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.*;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.progress.VerificationMode;

class StrictOrderVerifier implements Strictly {
    
    private final Reporter reporter = new Reporter();
    private final List<Object> mocksToBeVerifiedSrictly = new LinkedList<Object>();
    
    public void addMockToBeVerifiedStrictly(Object mock) {
        mocksToBeVerifiedSrictly.add(mock);
    }
    
    public <T> T verify(T mock) {
        return this.verify(mock, 1);
    }
    
    //TODO get rid of interface with int
    public <T> T verify(T mock, int wantedNumberOfInvocations) {
        return this.verify(mock, VerificationMode.strict(wantedNumberOfInvocations, mocksToBeVerifiedSrictly));
    }
    
    public <T> T verify(T mock, VerificationMode verificationMode) {
        if (!mocksToBeVerifiedSrictly.contains(mock)) {
            reporter.strictlyRequiresFamiliarMock();
        }
        return Mockito.verify(mock, VerificationMode.strict(verificationMode.wantedCount(), mocksToBeVerifiedSrictly));
    }
}
