package org.mockito.internal;

import org.mockito.exceptions.UnfinishedVerificationException;

/**
 * state. therefore dangerous and may have nasty bugs.
 * TODO look at every method that changes state and make sure the state is cleared afterwards
 * 
 * @author sfaber
 */
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

    public synchronized MockitoExpectation removeControlToBeStubbed() {
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

    public synchronized boolean verificationScenario() {
        return verifyingModeLocal.get() != null; 
    }

    public synchronized VerifyingMode removeVerifyingMode() {
        VerifyingMode verifyingMode = verifyingModeLocal.get();
        verifyingModeLocal.set(null);
        return verifyingMode;
    }

    public synchronized void reportThrowableToBeSetOnVoidMethod(Throwable throwable) {
        throwableToBeSetOnVoidMethod.set(throwable);
    }

    public synchronized Throwable removeThrowableToBeSetOnVoidMethod() {
        Throwable throwable = throwableToBeSetOnVoidMethod.get();
        throwableToBeSetOnVoidMethod.set(null);
        return throwable;
    }

    public synchronized boolean settingThrowableOnVoidMethodScenario() {
        return throwableToBeSetOnVoidMethod.get() != null; 
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
