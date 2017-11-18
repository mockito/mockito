/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

abstract class ReturningStubInProgress<R> extends StubInProgress {

    public void thenReturn(R returnValue) {
        this.setAnswerAndInvokeMethod((_unused) -> returnValue);
    }
}
