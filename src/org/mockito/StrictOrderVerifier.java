/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.progress.VerificationMode;
import org.mockito.internal.progress.VerificationModeImpl;

/**
 * Allows verifying strictly. This class should not be exposed, hence default access.
 */
class StrictOrderVerifier implements Strictly {
    
    private final Reporter reporter = new Reporter();
    private final List<Object> mocksToBeVerifiedSrictly = new LinkedList<Object>();
    
    public StrictOrderVerifier(List<Object> mocksToBeVerifiedStrictly) {
        mocksToBeVerifiedSrictly.addAll(mocksToBeVerifiedStrictly);
    }

    public <T> T verify(T mock) {
        return this.verify(mock, VerificationModeImpl.times(1));
    }
    
    public <T> T verify(T mock, VerificationMode mode) {
        if (!mocksToBeVerifiedSrictly.contains(mock)) {
            reporter.strictlyRequiresFamiliarMock();
        }
        Integer wantedCount = ((VerificationModeImpl) mode).wantedCount();
        return Mockito.verify(mock, VerificationModeImpl.strict(wantedCount, mocksToBeVerifiedSrictly));
    }
}
