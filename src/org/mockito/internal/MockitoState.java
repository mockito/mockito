package org.mockito.internal;

import org.mockito.exceptions.UnfinishedVerificationException;

@SuppressWarnings("unchecked")
public class MockitoState {
    
    static MockitoState INSTANCE = new MockitoState();
    
    private final ThreadLocal<MockitoControl> lastControl = new ThreadLocal<MockitoControl>();
    private final ThreadLocal<VerifyingMode> verifyingModeLocal = new ThreadLocal<VerifyingMode>();
//    private final ThreadLocal<Object> stubbingModeLocal = new ThreadLocal<Object>();

    MockitoState() {}
    
    public static MockitoState instance() {
        return INSTANCE;
    }
    
    public synchronized void reportLastControl(MockitoControl mockitoControl) {
        lastControl.set(mockitoControl);
    }

    public synchronized MockitoExpectation pullControlToBeStubbed() {
        MockitoControl control = lastControl.get();
        lastControl.set(null);
        return control;
    }
    
    public synchronized void verifyingStarted(VerifyingMode verify) {
        checkForUnfinishedVerification();
        verifyingModeLocal.set(verify);
    }

    public void checkForUnfinishedVerification() {
        if (verifyingModeLocal.get() != null) {
            throw new UnfinishedVerificationException();
        }
    }

    public synchronized VerifyingMode pullVerifyingMode() {
        VerifyingMode verifyingMode = verifyingModeLocal.get();
        verifyingModeLocal.set(null);
        return verifyingMode;
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
