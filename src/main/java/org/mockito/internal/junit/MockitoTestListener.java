package org.mockito.internal.junit;

import java.util.Collection;

interface MockitoTestListener {

    void beforeTest(Object testClassInstance, String testMethodName);

    void afterTest(Collection<Object> mocks, Throwable problem);
}
