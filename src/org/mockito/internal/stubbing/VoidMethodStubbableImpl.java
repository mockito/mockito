/**
 * 
 */
package org.mockito.internal.stubbing;

import org.mockito.stubbing.Answer;

public class VoidMethodStubbableImpl<T> implements VoidMethodStubbable<T> {

    private final T mock;
    private final MockitoStubber mockitoStubber;

    public VoidMethodStubbableImpl(T mock, MockitoStubber mockitoStubber) {
        this.mock = mock;
        this.mockitoStubber = mockitoStubber;
    }

    public VoidMethodStubbable<T> toThrow(Throwable throwable) {
        mockitoStubber.addAnswerForVoidMethod(new ThrowsException(throwable));
        return this;
    }

    public VoidMethodStubbable<T> toReturn() {
        mockitoStubber.addAnswerForVoidMethod(new DoesNothing());
        return this;
    }

    public VoidMethodStubbable<T> toAnswer(Answer<?> answer) {
        mockitoStubber.addAnswerForVoidMethod(answer);
        return this;
    }

    public T on() {
        return mock;
    }
}