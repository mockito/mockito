/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.mockito.exceptions.Exceptions;

@SuppressWarnings("unchecked")
public class MockitoState {
    
    private static ThreadLocal<MockitoState> INSTANCE = new ThreadLocal<MockitoState>();
        
    private MockControl lastControl;
    private VerifyingMode verifyingMode;
    private int invocationSequenceNumber = 1;
    private boolean stubbingInProgress = false;

    public static MockitoState instance() {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new MockitoState());
        }
        return INSTANCE.get();
    }
    
    public void reportControlForStubbing(MockControl mockControl) {
        lastControl = mockControl;
    }

    public MockitoExpectation pullControlToBeStubbed() {
        MockControl temp = lastControl;
        lastControl = null;
        return temp;
    }
    
    public void verifyingStarted(VerifyingMode verify) {
        validateState();
        verifyingMode = verify;
    }

    public VerifyingMode pullVerifyingMode() {
        VerifyingMode temp = verifyingMode;
        verifyingMode = null;
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
        if (verifyingMode != null) {
            verifyingMode = null;
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
        return  "lastControl: " + lastControl + 
        ", verifyingMode: " + verifyingMode +
        ", invocationSequenceNumber: " + invocationSequenceNumber +
        ", stubbingInProgress: " + stubbingInProgress;
    }

    void reset() {
        stubbingInProgress = false;
        verifyingMode = null;
        invocationSequenceNumber = 1;
    }
}
