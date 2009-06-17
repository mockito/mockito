/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.VerificationMode;

public class ThreadSafeMockingProgress implements MockingProgress {
    
    private static ThreadLocal<MockingProgress> mockingProgress = new ThreadLocal<MockingProgress>();

    static MockingProgress threadSafely() {
        if (mockingProgress.get() == null) {
            mockingProgress.set(new MockingProgressImpl());
        }
        return mockingProgress.get();
    }
    
    public void reportOngoingStubbing(IOngoingStubbing iOngoingStubbing) {
        threadSafely().reportOngoingStubbing(iOngoingStubbing);
    }

    public IOngoingStubbing pullOngoingStubbing() {
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

    public ArgumentMatcherStorage getArgumentMatcherStorage() {
        return threadSafely().getArgumentMatcherStorage();
    }

    public DebuggingInfo getDebuggingInfo() {
        return threadSafely().getDebuggingInfo();
    }
}