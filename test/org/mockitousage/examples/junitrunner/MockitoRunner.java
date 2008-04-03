/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.junitrunner;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.mockito.MockitoAnnotations;

public class MockitoRunner extends JUnit4ClassRunner {

    public MockitoRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
    
    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        MockitoAnnotations.initMocks(test);
        return test;
    }
}