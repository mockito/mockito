package org.mockito.internal.junit;

public interface TestFinishedEvent {

    Throwable getFailure();

    Object getTestClassInstance();

    String getTestMethodName();

}
