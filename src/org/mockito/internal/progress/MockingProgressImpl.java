/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.exceptions.Reporter;

@SuppressWarnings("unchecked")
public class MockingProgressImpl implements MockingProgress {
    
    private final Reporter reporter = new Reporter();
    
    private OngoingStubbing ongoingStubbing;
    private VerificationModeImpl verificationMode;
    private int invocationSequenceNumber = 1;
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
        verificationMode = (VerificationModeImpl) verify;
    }

    public VerificationModeImpl pullVerificationMode() {
        VerificationModeImpl temp = verificationMode;
        verificationMode = null;
        return temp;
    }

    public int nextSequenceNumber() {
        return invocationSequenceNumber++;
    }

    public void stubbingStarted() {
        validateState();
        stubbingInProgress = true;
    }

    public void validateState() {
        if (verificationMode != null) {
            verificationMode = null;
            reporter.unfinishedVerificationException();
        }
        
        if (stubbingInProgress) {
            stubbingInProgress = false;
            reporter.unfinishedStubbing();
        }
    }

    public void stubbingCompleted() {
        stubbingInProgress = false;
    }
    
    public String toString() {
        return  "ongoingStubbing: " + ongoingStubbing + 
        ", verificationMode: " + verificationMode +
        ", invocationSequenceNumber: " + invocationSequenceNumber +
        ", stubbingInProgress: " + stubbingInProgress;
    }

    public void reset() {
        stubbingInProgress = false;
        verificationMode = null;
        invocationSequenceNumber = 1;
    }
}