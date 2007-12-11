package org.mockito.internal.state;



//TODO name should be something like that: MockingState
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