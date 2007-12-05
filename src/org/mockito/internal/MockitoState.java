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
    private VerifyingMode verifyingModeLocal;
    private int invocationSequenceNumber = 1;
    private boolean stubbingModeLocal = false;

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
        verifyingModeLocal = verify;
    }

    public VerifyingMode pullVerifyingMode() {
        VerifyingMode temp = verifyingModeLocal;
        verifyingModeLocal = null;
        return temp;
    }

    public int nextSequenceNumber() {
        return invocationSequenceNumber++;
    }

    public void stubbingStarted() {
        validateState();
        stubbingModeLocal = true;
    }

    public void validateState() {
        if (verifyingModeLocal != null) {
            verifyingModeLocal = null;
            Exceptions.unfinishedVerificationException();
        }
        
        if (stubbingModeLocal) {
            stubbingModeLocal = false;
            Exceptions.unfinishedStubbing();
        }
    }

    public void stubbingCompleted() {
        stubbingModeLocal = false;
    }
    
    public String toString() {
        return  "lastControl: " + lastControl + 
                ", verifyingMode: " + verifyingModeLocal +
                ", invocationSequenceNumber: " + invocationSequenceNumber +
                ", stubbingModeLocal: " + stubbingModeLocal;
    }

    synchronized void reset() {
        stubbingModeLocal = false;
        verifyingModeLocal = null;
        invocationSequenceNumber = 1;
    }
}
