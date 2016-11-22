package org.mockito.internal.junit;

import org.mockito.listeners.MockCreationListener;

interface MockitoTestListener extends MockCreationListener {

    void beforeTest(Object testClassInstance, String testMethodName);

    void afterTest(Throwable problem);
}
