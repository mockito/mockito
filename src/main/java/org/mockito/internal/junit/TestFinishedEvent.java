package org.mockito.internal.junit;

/**
 * Created by sfaber on 11/24/16.
 */
interface TestFinishedEvent {

    Throwable getFailure();

    Object getTestClassInstance();

    String getTestMethodName();

}
