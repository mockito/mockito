/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

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
