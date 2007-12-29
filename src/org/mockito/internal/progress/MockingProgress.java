/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

@SuppressWarnings("unchecked")
public interface MockingProgress {

    void reportOngoingStubbing(OngoingStubbing ongoingStubbing);

    OngoingStubbing pullOngoingStubbing();

    void verificationStarted(VerificationMode verificationMode);

    VerificationModeImpl pullVerificationMode();

    void stubbingStarted();

    void stubbingCompleted();
    
    int nextSequenceNumber();

    void validateState();

    void reset();
}