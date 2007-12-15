/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

@SuppressWarnings("unchecked")
public interface MockingProgress {

    void reportStubable(OngoingStubbing ongoingStubbing);

    OngoingStubbing pullStubable();

    void verificationStarted(VerificationMode verificationMode);

    VerificationMode pullVerificationMode();

    void stubbingStarted();

    void stubbingCompleted();
    
    int nextSequenceNumber();

    void validateState();

    void reset();
}