package org.mockito.internal;

/**
 * static state. therefore dangerous and may have nasty bugs.
 * TODO look at every method that changes state and make sure the state is cleared afterwards
 * 
 * @author sfaber
 */
public class MockitoOperations {
    
    private final static ThreadLocal<MockitoControl> controlForStubbing = new ThreadLocal<MockitoControl>();
    private final static ThreadLocal<VerifyingMode> verifyingModeLocal = new ThreadLocal<VerifyingMode>();
    private final static ThreadLocal<Throwable> throwableToBeSetOnVoidMethod = new ThreadLocal<Throwable>();

    public static synchronized void reportLastControlForStubbing(MockitoControl mockitoControl) {
        controlForStubbing.set(mockitoControl);
    }

    public static synchronized MockitoExpectation controlToBeStubbed() {
        return controlForStubbing.get();
    }
    
    public static synchronized void reportVerifyingMode(VerifyingMode verify) {
        verifyingModeLocal.set(verify);
    }

    public static synchronized boolean mockVerificationScenario() {
        return verifyingModeLocal.get() != null; 
    }

    public static synchronized VerifyingMode removeVerifyingMode() {
        VerifyingMode verifyingMode = verifyingModeLocal.get();
        verifyingModeLocal.set(null);
        return verifyingMode;
    }

    public static void reportThrowableToBeSetOnVoidMethod(Throwable throwable) {
        throwableToBeSetOnVoidMethod.set(throwable);
    }

    public static Throwable removeThrowableToBeSetOnVoidMethod() {
        Throwable throwable = throwableToBeSetOnVoidMethod.get();
        throwableToBeSetOnVoidMethod.set(null);
        return throwable;
    }

    public static boolean settingThrowableOnVoidMethodScenario() {
        return throwableToBeSetOnVoidMethod.get() != null; 
    }
}
