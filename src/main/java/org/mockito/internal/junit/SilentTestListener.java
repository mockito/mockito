package org.mockito.internal.junit;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collection;

class SilentTestListener implements MockitoTestListener {
    public void beforeTest(Object testClassInstance, String testMethodName) {
        MockitoAnnotations.initMocks(testClassInstance);
    }

    public void afterTest(Collection<Object> mocks, Throwable problem) {
        if (problem == null) {
            //Validate only when there is no other problem to avoid reporting multiple problems
            Mockito.validateMockitoUsage();
        }
    }
}
