/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.progress.VerificationMode;

class StrictOrderVerifier implements Strictly {
    
    private final Reporter reporter = new Reporter();
    private final List<Object> mocksToBeVerifiedSrictly = new LinkedList<Object>();
    
    public void addMockToBeVerifiedStrictly(Object mock) {
        mocksToBeVerifiedSrictly.add(mock);
    }
    
    public <T> T verify(T mock) {
        return this.verify(mock, VerificationMode.times(1));
    }
    
    public <T> T verify(T mock, VerificationMode verificationMode) {
        if (!mocksToBeVerifiedSrictly.contains(mock)) {
            reporter.strictlyRequiresFamiliarMock();
        }
        return Mockito.verify(mock, VerificationMode.strict(verificationMode.wantedCount(), mocksToBeVerifiedSrictly));
    }
}
