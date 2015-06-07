/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import java.io.Serializable;

import org.mockito.internal.listeners.MockingProgressListener;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("rawtypes")
public class ThreadSafeMockingProgress implements MockingProgress, Serializable {
    
    private static final long serialVersionUID = 6839454041642082618L;
    private static final ThreadLocal<MockingProgress> MOCKING_PROGRESS = new ThreadLocal<MockingProgress>();

    static MockingProgress threadSafely() {
        if (MOCKING_PROGRESS.get() == null) {
            MOCKING_PROGRESS.set(new MockingProgressImpl());
        }
        return MOCKING_PROGRESS.get();
    }
    
    public void reportOngoingStubbing(final IOngoingStubbing iOngoingStubbing) {
        threadSafely().reportOngoingStubbing(iOngoingStubbing);
    }

    public IOngoingStubbing pullOngoingStubbing() {
        return threadSafely().pullOngoingStubbing();
    }
    
    public void verificationStarted(final VerificationMode verify) {
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

    public void stubbingCompleted(final Invocation invocation) {
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
    
    public void mockingStarted(final Object mock, final Class classToMock) {
        threadSafely().mockingStarted(mock, classToMock);
    }

    public void setListener(final MockingProgressListener listener) {
        threadSafely().setListener(listener);
    }
}