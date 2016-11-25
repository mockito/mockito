package org.mockito.internal.junit;

/**
 * Created by sfaber on 11/24/16.
 */
public class DefaultTestFinishedEvent implements TestFinishedEvent {
    private final Object testClassInstance;
    private final String testMethodName;
    private final Throwable testFailure;

    public DefaultTestFinishedEvent(Object testClassInstance, String testMethodName, Throwable testFailure) {
        this.testClassInstance = testClassInstance;
        this.testMethodName = testMethodName;
        this.testFailure = testFailure;
    }

    public Throwable getFailure() {
        return testFailure;
    }

    public Object getTestClassInstance() {
        return testClassInstance;
    }

    public String getTestMethodName() {
        return testMethodName;
    }
}