/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class MockAwareVerificationMode implements VerificationMode {

    private final Object mock;
    private final VerificationMode mode;
    private final MockingProgress mockingProgress;

    public MockAwareVerificationMode(Object mock, VerificationMode mode, MockingProgress mockingProgress) {
        this.mock = mock;
        this.mode = mode;
        this.mockingProgress = mockingProgress;
    }

    public void verify(VerificationData data) {
        try {
            mode.verify(data);
        } catch (RuntimeException e) {
            fireVerificationEvent( data, e);
            throw e;
        } catch (Error e) {
            fireVerificationEvent(data, e);
            throw e;
        }

        fireVerificationEvent(data,null);
    }


    private void fireVerificationEvent(VerificationData data, Throwable error) {
    	mockingProgress.fireVerificationEvent(new VerificationEventImpl(mock, mode, data, error));
    }

    public Object getMock() {
        return mock;
    }

    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
