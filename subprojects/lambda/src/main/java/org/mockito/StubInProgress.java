/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.stubbing.Answer;

abstract class StubInProgress<R> {

    public void thenReturn(R returnValue) {
        MockitoLambdaHandlerImpl.answerValue = (_unused) -> returnValue;

        this.invokeMethod();
    }

    public void thenThrow(Throwable e) {
        MockitoLambdaHandlerImpl.answerValue = (_unused) -> {throw e;};

        this.invokeMethod();
    }

    public void thenAnswer(Answer<?> answer) {
        MockitoLambdaHandlerImpl.answerValue = answer;

        this.invokeMethod();
    }

    abstract void invokeMethod();

}
