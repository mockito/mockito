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