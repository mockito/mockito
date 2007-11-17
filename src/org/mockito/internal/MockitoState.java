package org.mockito.internal;

/**
 * state. therefore dangerous and may have nasty bugs.
 * TODO look at every method that changes state and make sure the state is cleared afterwards
 * 
 * @author sfaber
 */
public class MockitoState {
    
    static MockitoState INSTANCE = new MockitoState();
    
    private final ThreadLocal<MockitoControl> controlForStubbing = new ThreadLocal<MockitoControl>();
    private final ThreadLocal<VerifyingMode> verifyingModeLocal = new ThreadLocal<VerifyingMode>();
    private final ThreadLocal<Throwable> throwableToBeSetOnVoidMethod = new ThreadLocal<Throwable>();
    private final ThreadLocal<Object> stubbingModeLocal = new ThreadLocal<Object>();

    MockitoState() {}
    
    public static MockitoState instance() {
        return INSTANCE;
    }
    
    public synchronized void reportLastControlForStubbing(MockitoControl mockitoControl) {
        controlForStubbing.set(mockitoControl);
    }

    public synchronized MockitoExpectation controlToBeStubbed() {
        return controlForStubbing.get();
    }
    
    public synchronized void verifyingStarted(VerifyingMode verify) {
        verifyingModeLocal.set(verify);
    }

    public synchronized boolean mockVerificationScenario() {
        return verifyingModeLocal.get() != null; 
    }

    public synchronized VerifyingMode verifyingCompleted() {
        VerifyingMode verifyingMode = verifyingModeLocal.get();
        verifyingModeLocal.set(null);
        return verifyingMode;
    }

    public void reportThrowableToBeSetOnVoidMethod(Throwable throwable) {
        throwableToBeSetOnVoidMethod.set(throwable);
    }

    public Throwable removeThrowableToBeSetOnVoidMethod() {
        Throwable throwable = throwableToBeSetOnVoidMethod.get();
        throwableToBeSetOnVoidMethod.set(null);
        return throwable;
    }

    public boolean settingThrowableOnVoidMethodScenario() {
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
