package org.mockito.internal.junit;

import org.mockito.mock.MockCreationSettings;

class NoOpTestListener implements MockitoTestListener {

    public void beforeTest(Object testClassInstance, String testMethodName) {}

    public void afterTest(Throwable testFailure) {}

    public void onMockCreated(Object mock, MockCreationSettings settings) {}
}
