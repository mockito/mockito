/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;



//TODO verificationMode instead verifying
@SuppressWarnings("unchecked")
public interface MockingProgress {

    void reportStubable(OngoingStubbing ongoingStubbing);

    OngoingStubbing pullStubable();

    void verifyingStarted(OngoingVerifyingMode verifyingMode);

    OngoingVerifyingMode pullVerifyingMode();

    void stubbingStarted();

    void stubbingCompleted();
    
    int nextSequenceNumber();

    void validateState();

    void reset();
}