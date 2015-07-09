/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class DummyVerificationMode implements VerificationMode {
    public void verify(VerificationData data) {
    }
    
    public VerificationMode description(String description) {
        return new DummyVerificationMode();
    }
}
