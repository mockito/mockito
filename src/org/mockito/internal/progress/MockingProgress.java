/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.mockito.MockSettings;
import org.mockito.internal.listeners.MockingProgressListener;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("unchecked")
public interface MockingProgress {
    
    void reportOngoingStubbing(IOngoingStubbing iOngoingStubbing);

    IOngoingStubbing pullOngoingStubbing();

    void verificationStarted(VerificationMode verificationMode);

    VerificationMode pullVerificationMode();

    void stubbingStarted();

    void stubbingCompleted(Invocation invocation);
    
    void validateState();

    void reset();

    /**
     * Removes ongoing stubbing so that in case the framework is misused
     * state validation errors are more accurate
     */
    void resetOngoingStubbing();

    ArgumentMatcherStorage getArgumentMatcherStorage();
    
    void mockingStarted(Object mock, Class classToMock);

    void setListener(MockingProgressListener listener);
}