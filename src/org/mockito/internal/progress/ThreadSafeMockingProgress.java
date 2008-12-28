/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.VerificationMode;

@SuppressWarnings("unchecked")
public class ThreadSafeMockingProgress implements MockingProgress {
    
    private static ThreadLocal<MockingProgress> mockingProgress = new ThreadLocal<MockingProgress>();

    static MockingProgress threadSafely() {
        if (mockingProgress.get() == null) {
            mockingProgress.set(new MockingProgressImpl());
        }
        return mockingProgress.get();
    }
    
    public void reportOngoingStubbing(OngoingStubbing ongoingStubbing) {
        threadSafely().reportOngoingStubbing(ongoingStubbing);
    }

    public OngoingStubbing pullOngoingStubbing() {
        return threadSafely().pullOngoingStubbing();
    }
    
    public void verificationStarted(VerificationMode verify) {
        threadSafely().verificationStarted(verify);
    }

    public VerificationMode pullVerificationMode() {
        return threadSafely().pullVerificationMode();
    }

    public void stubbingStarted() {
        threadSafely().stubbingStarted();
    }

    public void validateState() {
        threadSafely().validateState();
    }

    public void stubbingCompleted(Invocation invocation) {
        threadSafely().stubbingCompleted(invocation);
    }
    
    public String toString() {
        return threadSafely().toString();
    }

    public void reset() {
        threadSafely().reset();
    }

    public void resetOngoingStubbing() {
        threadSafely().resetOngoingStubbing();
    }

    public List<Invocation> pullStubbedInvocations() {
        return threadSafely().pullStubbedInvocations();
    }

    public LastArguments getLastArguments() {
        return threadSafely().getLastArguments();
    }
}