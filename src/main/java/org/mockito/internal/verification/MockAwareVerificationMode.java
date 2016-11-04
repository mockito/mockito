/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class MockAwareVerificationMode implements VerificationMode {

    private final Object mock;
    private final VerificationMode mode;

    public MockAwareVerificationMode(Object mock, VerificationMode mode) {
        this.mock = mock;
        this.mode = mode;
    }

    public void verify(VerificationData data) {
        mode.verify(data);
    }

    public Object getMock() {
        return mock;
    }

    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}