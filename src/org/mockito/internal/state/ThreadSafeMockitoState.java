/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.state;



@SuppressWarnings("unchecked")
public class ThreadSafeMockitoState implements MockitoState {
    
    private static ThreadLocal<MockitoState> mockitoState = new ThreadLocal<MockitoState>();

    static MockitoState threadSafely() {
        if (mockitoState.get() == null) {
            mockitoState.set(new MockitoStateImpl());
        }
        return mockitoState.get();
    }
    
    public void reportStubable(OngoingStubbing ongoingStubbing) {
        threadSafely().reportStubable(ongoingStubbing);
    }

    public OngoingStubbing pullStubable() {
        return threadSafely().pullStubable();
    }
    
    public void verifyingStarted(OngoingVerifyingMode verify) {
        threadSafely().verifyingStarted(verify);
    }

    public OngoingVerifyingMode pullVerifyingMode() {
        return threadSafely().pullVerifyingMode();
    }

    public int nextSequenceNumber() {
        return threadSafely().nextSequenceNumber();
    }

    public void stubbingStarted() {
        threadSafely().stubbingStarted();
    }

    public void validateState() {
        threadSafely().validateState();
    }

    public void stubbingCompleted() {
        threadSafely().stubbingCompleted();
    }
    
    public String toString() {
        return threadSafely().toString();
    }

    public void reset() {
        threadSafely().reset();
    }
}
