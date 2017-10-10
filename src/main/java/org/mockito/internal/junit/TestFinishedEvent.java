/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

public interface TestFinishedEvent {

    Throwable getFailure();

    Object getTestClassInstance();

    String getTestMethodName();

}
