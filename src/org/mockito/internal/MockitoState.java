package org.mockito.internal;

@SuppressWarnings("unchecked")
public interface MockitoState {

    void reportControlForStubbing(MockControl mockControl);

    MockitoExpectation pullControlToBeStubbed();

    void verifyingStarted(VerifyingMode verify);

    VerifyingMode pullVerifyingMode();

    void stubbingStarted();

    void stubbingCompleted();
    
    int nextSequenceNumber();

    void validateState();

    void reset();
}