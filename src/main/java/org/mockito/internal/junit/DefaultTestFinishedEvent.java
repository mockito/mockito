/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

public class DefaultTestFinishedEvent implements TestFinishedEvent {
    private final Class<?> testClass;
    private final String testMethodName;
    private final Throwable testFailure;

    public DefaultTestFinishedEvent(Class<?> testClass, String testMethodName, Throwable testFailure) {
        this.testClass = testClass;
        this.testMethodName = testMethodName;
        this.testFailure = testFailure;
    }

    @Override
    public Throwable getFailure() {
        return testFailure;
    }

    @Override
    public String getTestName() {
        return testClass.getSimpleName() + "." + testMethodName;
    }
}
