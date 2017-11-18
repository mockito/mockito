/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.stubbing.Answer;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

abstract class StubInProgress {

    protected final ArgumentMatcherStorage argumentMatcherStorage = mockingProgress().getArgumentMatcherStorage();

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

        try {
            this.invokeMethod();
        } catch (NullPointerException e) {
            this.resetState();
            throw new AutoBoxingNullPointerException(e);
        } catch (CouldNotConstructObjectException e) {
            this.resetState();
            throw e;
        }
    }

    abstract void invokeMethod();
}
