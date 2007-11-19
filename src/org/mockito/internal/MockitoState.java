package org.mockito.internal;

import org.mockito.exceptions.UnfinishedVerificationException;

@SuppressWarnings("unchecked")
public class MockitoState {
    
    static MockitoState INSTANCE = new MockitoState();
    
    private final ThreadLocal<MockitoControl> controlForStubbing = new ThreadLocal<MockitoControl>();
    private final ThreadLocal<VerifyingMode> verifyingModeLocal = new ThreadLocal<VerifyingMode>();
    private final ThreadLocal<Throwable> throwableToBeSetOnVoidMethod = new ThreadLocal<Throwable>();
//    private final ThreadLocal<Object> stubbingModeLocal = new ThreadLocal<Object>();

    MockitoState() {}
    
    public static MockitoState instance() {
        return INSTANCE;
    }
    
    public synchronized void reportLastControl(MockitoControl mockitoControl) {
        controlForStubbing.set(mockitoControl);
    }

    public synchronized MockitoExpectation pullControlToBeStubbed() {
        MockitoControl control = controlForStubbing.get();
        controlForStubbing.set(null);
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

    public synchronized void reportThrowableToBeSetOnVoidMethod(Throwable throwable) {
        //TODO refactor so we don't use static state to keep the throwable. we 
        //can set it directly to mockcontrol or something and keep this pseudo static class thinner
        
        throwableToBeSetOnVoidMethod.set(throwable);
    }

    public synchronized Throwable pullThrowableToBeSetOnVoidMethod() {
        Throwable throwable = throwableToBeSetOnVoidMethod.get();
        throwableToBeSetOnVoidMethod.set(null);
        return throwable;
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
