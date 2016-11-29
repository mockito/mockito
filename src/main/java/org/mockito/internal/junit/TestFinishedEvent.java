package org.mockito.internal.junit;

interface TestFinishedEvent {

    Throwable getFailure();

    Object getTestClassInstance();

    String getTestMethodName();

}
