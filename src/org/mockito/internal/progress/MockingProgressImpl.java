/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.VerificationMode;

@SuppressWarnings("unchecked")
public class MockingProgressImpl implements MockingProgress {
    
    private final Reporter reporter = new Reporter();
    private final ArgumentMatcherStorage argumentMatcherStorage = new ArgumentMatcherStorageImpl();
    
    private final DebuggingInfo debuggingInfo = new DebuggingInfo();

    OngoingStubbing ongoingStubbing;
    private VerificationMode verificationMode;
    private boolean stubbingInProgress = false;

    public void reportOngoingStubbing(OngoingStubbing ongoingStubbing) {
        this.ongoingStubbing = ongoingStubbing;
    }

    public OngoingStubbing pullOngoingStubbing() {
        OngoingStubbing temp = ongoingStubbing;
        ongoingStubbing = null;
        return temp;
    }
    
    public void verificationStarted(VerificationMode verify) {
        validateState();
        resetOngoingStubbing();
        verificationMode = (VerificationMode) verify;
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.MockingProgress#resetOngoingStubbing()
     */
    public void resetOngoingStubbing() {
        ongoingStubbing = null;
    }

    public VerificationMode pullVerificationMode() {
        VerificationMode temp = verificationMode;
        verificationMode = null;
        return temp;
    }

    public void stubbingStarted() {
        validateState();
        stubbingInProgress = true;
    }

    public void validateState() {
        //State is cool when GlobalConfiguration is already loaded
        //this cannot really be tested functionally because I cannot dynamically mess up org.mockito.configuration.MockitoConfiguration class 
        GlobalConfiguration.validate();
        
        if (verificationMode != null) {
            verificationMode = null;
            reporter.unfinishedVerificationException();
        }
        
        if (stubbingInProgress) {
            stubbingInProgress = false;
            reporter.unfinishedStubbing();
        }
      
        getArgumentMatcherStorage().validateState();
    }

    public void stubbingCompleted(Invocation invocation) {
        debuggingInfo.addStubbedInvocation(invocation);        
        stubbingInProgress = false;
    }
    
    public String toString() {
        return  "ongoingStubbing: " + ongoingStubbing + 
        ", verificationMode: " + verificationMode +
        ", stubbingInProgress: " + stubbingInProgress;
    }

    public void reset() {
        stubbingInProgress = false;
        verificationMode = null;
        getArgumentMatcherStorage().reset();
    }

    public ArgumentMatcherStorage getArgumentMatcherStorage() {
        return argumentMatcherStorage;
    }

    public DebuggingInfo getDebuggingInfo() {
        return debuggingInfo;
    }
}