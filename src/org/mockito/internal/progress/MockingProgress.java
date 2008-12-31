/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.VerificationMode;

@SuppressWarnings("unchecked")
public interface MockingProgress {
    
    void reportOngoingStubbing(OngoingStubbing ongoingStubbing);

    OngoingStubbing pullOngoingStubbing();

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

    DebuggingInfo getDebuggingInfo();
}