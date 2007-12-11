/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.state;

import org.mockito.exceptions.Exceptions;

@SuppressWarnings("unchecked")
public class MockingProgressImpl implements MockingProgress {
    
    private OngoingStubbing ongoingStubbing;
    private OngoingVerifyingMode ongoingVerifyingMode;
    private int invocationSequenceNumber = 1;
    private boolean stubbingInProgress = false;

    public void reportStubable(OngoingStubbing ongoingStubbing) {
        this.ongoingStubbing = ongoingStubbing;
    }

    public OngoingStubbing pullStubable() {
        OngoingStubbing temp = ongoingStubbing;
        ongoingStubbing = null;
        return temp;
    }
    
    public void verifyingStarted(OngoingVerifyingMode verify) {
        validateState();
        ongoingVerifyingMode = verify;
    }

    public OngoingVerifyingMode pullVerifyingMode() {
        OngoingVerifyingMode temp = ongoingVerifyingMode;
        ongoingVerifyingMode = null;
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
        if (ongoingVerifyingMode != null) {
            ongoingVerifyingMode = null;
            Exceptions.unfinishedVerificationException();
        }
        
        if (stubbingInProgress) {
            stubbingInProgress = false;
            Exceptions.unfinishedStubbing();
        }
    }

    public void stubbingCompleted() {
        stubbingInProgress = false;
    }
    
    public String toString() {
        return  "ongoingStubbing: " + ongoingStubbing + 
        ", ongoingVerifyingMode: " + ongoingVerifyingMode +
        ", invocationSequenceNumber: " + invocationSequenceNumber +
        ", stubbingInProgress: " + stubbingInProgress;
    }

    public void reset() {
        stubbingInProgress = false;
        ongoingVerifyingMode = null;
        invocationSequenceNumber = 1;
    }
}
