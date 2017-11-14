/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.stubbing.Answer;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

abstract class StubInProgress<R> {

    protected final ArgumentMatcherStorage argumentMatcherStorage = mockingProgress().getArgumentMatcherStorage();

    public void thenReturn(R returnValue) {
        this.setAnswerAndInvokeMethod((_unused) -> returnValue);
    }

    public void thenThrow(Throwable e) {
        this.setAnswerAndInvokeMethod((_unused) -> {throw e;});
    }

    public void thenOldAnswer(Answer<?> answer) {
        this.setAnswerAndInvokeMethod(answer);
    }

    protected void resetState() {
        argumentMatcherStorage.reset();
    }

    protected void setAnswerAndInvokeMethod(Answer<?> answer) {
        MockitoLambdaHandlerImpl.answerValue = answer;

        this.invokeMethod();
    }

    abstract void invokeMethod();

}
