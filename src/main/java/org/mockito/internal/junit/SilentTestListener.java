package org.mockito.internal.junit;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.mock.MockCreationSettings;

class SilentTestListener implements MockitoTestListener {

    public void beforeTest(Object testClassInstance, String testMethodName) {
        MockitoAnnotations.initMocks(testClassInstance);
    }

    public void afterTest(Throwable testFailure) {
        if (testFailure == null) {
            //Validate only when there is no test failure to avoid reporting multiple problems
            Mockito.validateMockitoUsage();
        }
    }

    public void onMockCreated(Object mock, MockCreationSettings settings) {}
}
