/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.mockito.exceptions.*;

@SuppressWarnings("unchecked")
public class MockitoState {
    
    //TODO this has to be thready singleton
    static MockitoState INSTANCE = new MockitoState();
    
    private final ThreadLocal<MockControl> lastControl = new ThreadLocal<MockControl>();
    private final ThreadLocal<VerifyingMode> verifyingModeLocal = new ThreadLocal<VerifyingMode>();
    private final ThreadLocal<Integer> invocationSequenceNumber = new ThreadLocal<Integer>();
    private final ThreadLocal<Object> stubbingModeLocal = new ThreadLocal<Object>();

    MockitoState() {}
    
    public static MockitoState instance() {
        return INSTANCE;
    }
    
    public synchronized void reportControlForStubbing(MockControl mockControl) {
        lastControl.set(mockControl);
    }

    public synchronized MockitoExpectation pullControlToBeStubbed() {
        MockControl control = lastControl.get();
        lastControl.set(null);
        return control;
    }
    
    public synchronized void verifyingStarted(VerifyingMode verify) {
        validateState();
        verifyingModeLocal.set(verify);
    }

    public synchronized VerifyingMode pullVerifyingMode() {
        VerifyingMode verifyingMode = verifyingModeLocal.get();
        verifyingModeLocal.set(null);
        return verifyingMode;
    }

    public synchronized int nextSequenceNumber() {
        if (invocationSequenceNumber.get() == null) {
            invocationSequenceNumber.set(1);
            return 1;
        } else {
            int next = invocationSequenceNumber.get() + 1;
            invocationSequenceNumber.set(next);
            return next;
        }
    }

    public synchronized void stubbingStarted() {
        validateState();
        stubbingModeLocal.set(new Object());
    }

    public synchronized void validateState() {
        if (verifyingModeLocal.get() != null) {
            verifyingModeLocal.set(null);
            Exceptions.unfinishedVerificationException();
        }
        
        if (stubbingModeLocal.get() != null) {
            stubbingModeLocal.set(null);
            Exceptions.unfinishedStubbing();
        }
    }

    public synchronized void stubbingCompleted() {
        stubbingModeLocal.set(null);
    }
    
    public String toString() {
        return  "lastControl: " + lastControl.get() + 
                ", verifyingMode: " + verifyingModeLocal.get() +
                ", invocationSequenceNumber: " + invocationSequenceNumber.get() +
                ", stubbingModeLocal: " + stubbingModeLocal.get();
    }
}
