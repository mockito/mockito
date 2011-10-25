/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

public class VoidMethodStubbableImpl<T> implements VoidMethodStubbable<T> {
    private final T mock;
    private InvocationContainerImpl invocationContainerImpl;

    public VoidMethodStubbableImpl(T mock, InvocationContainerImpl invocationContainerImpl) {
        this.mock = mock;
        this.invocationContainerImpl = invocationContainerImpl;
    }

    public VoidMethodStubbable<T> toThrow(Throwable throwable) {
        invocationContainerImpl.addAnswerForVoidMethod(new ThrowsException(throwable));
        return this;
    }

    public VoidMethodStubbable<T> toReturn() {
        invocationContainerImpl.addAnswerForVoidMethod(new DoesNothing());
        return this;
    }

    public VoidMethodStubbable<T> toAnswer(Answer<?> answer) {
        invocationContainerImpl.addAnswerForVoidMethod(answer);
        return this;
    }

    public T on() {
        return mock;
    }
}