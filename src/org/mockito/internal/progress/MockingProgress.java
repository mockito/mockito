/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.internal.verification.api.VerificationMode;

@SuppressWarnings("unchecked")
public interface MockingProgress {
    
    void reportOngoingStubbing(OngoingStubbing ongoingStubbing);

    OngoingStubbing pullOngoingStubbing();

    void verificationStarted(VerificationMode verificationMode);

    VerificationMode pullVerificationMode();

    void stubbingStarted();

    void stubbingCompleted();
    
    int nextSequenceNumber();

    void validateState();

    void reset();
}