/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;


@SuppressWarnings("unchecked")
public class ThreadSafeMockitoState implements MockitoState {
    
    private static ThreadLocal<MockitoState> mockitoState = new ThreadLocal<MockitoState>();

    static MockitoState get() {
        if (mockitoState.get() == null) {
            mockitoState.set(new MockitoStateImpl());
        }
        return mockitoState.get();
    }
    
    public void reportControlForStubbing(MockControl mockControl) {
        get().reportControlForStubbing(mockControl);
    }

    public MockitoExpectation pullControlToBeStubbed() {
        return get().pullControlToBeStubbed();
    }
    
    public void verifyingStarted(VerifyingMode verify) {
        get().verifyingStarted(verify);
    }

    public VerifyingMode pullVerifyingMode() {
        return get().pullVerifyingMode();
    }

    public int nextSequenceNumber() {
        return get().nextSequenceNumber();
    }

    public void stubbingStarted() {
        get().stubbingStarted();
    }

    public void validateState() {
        get().validateState();
    }

    public void stubbingCompleted() {
        get().stubbingCompleted();
    }
    
    public String toString() {
        return get().toString();
    }

    public void reset() {
        get().reset();
    }
}
