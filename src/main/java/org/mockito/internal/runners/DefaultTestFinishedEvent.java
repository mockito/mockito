package org.mockito.internal.runners;

import org.mockito.internal.junit.TestFinishedEvent;

class DefaultTestFinishedEvent implements TestFinishedEvent {

    private final Object testClassInstance;
    private final String testMethodName;
    private final Throwable failure;

    DefaultTestFinishedEvent(Object testClassInstance, String testMethodName, Throwable failure) {
        this.testClassInstance = testClassInstance;
        this.testMethodName = testMethodName;
        this.failure = failure;
    }

    @Override
    public Object getTestClassInstance() {
        return testClassInstance;
    }

    @Override
    public String getTestMethodName() {
        return testMethodName;
    }

    @Override
    public Throwable getFailure() {
        return failure;
    }
}
