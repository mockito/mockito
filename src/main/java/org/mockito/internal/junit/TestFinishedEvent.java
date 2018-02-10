/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

public interface TestFinishedEvent {

    Throwable getFailure();

    String getTestName();

}
