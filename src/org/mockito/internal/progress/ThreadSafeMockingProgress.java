/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import java.io.Serializable;

import org.mockito.MockSettings;
import org.mockito.internal.listeners.MockingProgressListener;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("unchecked")
public class ThreadSafeMockingProgress implements MockingProgress, Serializable {
    
    private static final long serialVersionUID = 6839454041642082618L;
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
    
    public void mockingStarted(Object mock, Class classToMock) {
        threadSafely().mockingStarted(mock, classToMock);
    }

    public void setListener(MockingProgressListener listener) {
        threadSafely().setListener(listener);
    }
}