/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.mockito.exceptions.UnfinishedVerificationException;

@SuppressWarnings("unchecked")
public class MockitoState {
    
    //TODO this has to be threaddy singleton
    static MockitoState INSTANCE = new MockitoState();
    
    private final ThreadLocal<MockControl> lastControl = new ThreadLocal<MockControl>();
    private final ThreadLocal<VerifyingMode> verifyingModeLocal = new ThreadLocal<VerifyingMode>();
    private final ThreadLocal<Integer> invocationSequenceNumber = new ThreadLocal<Integer>();
//    private final ThreadLocal<Object> stubbingModeLocal = new ThreadLoca<Object>();

    MockitoState() {}
    
    public static MockitoState instance() {
        return INSTANCE;
    }
    
    public synchronized void reportLastControl(MockControl mockControl) {
        lastControl.set(mockControl);
    }

    public synchronized MockitoExpectation pullControlToBeStubbed() {
        MockControl control = lastControl.get();
        lastControl.set(null);
        return control;
    }
    
    public synchronized void verifyingStarted(VerifyingMode verify) {
        checkForUnfinishedVerification();
        verifyingModeLocal.set(verify);
    }

    public synchronized void checkForUnfinishedVerification() {
        if (verifyingModeLocal.get() != null) {
            throw new UnfinishedVerificationException();
        }
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

//    public void stubbingStarted() {
//        stubbingModeLocal.set(new Object());
//    }

//    public boolean mockStubbingScenario() {
//        return stubbingModeLocal.get() != null;
//    }

//    public void stubbingCompleted() {
//        stubbingModeLocal.set(null);
//    }
}
