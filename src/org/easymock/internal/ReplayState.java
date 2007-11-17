/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

import org.easymock.ArgumentsMatcher;
import org.mockito.internal.*;

public class ReplayState implements IMocksControlState {

    private IMocksBehavior behavior;

    public ReplayState(IMocksBehavior behavior) {
        this.behavior = behavior;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        Result result = behavior.addActual(invocation);
        LastArguments.pushCurrentArguments(invocation.getArguments());
        try {
            try {
                return result.answer();
            } catch (Throwable t) {
                throw new ThrowableWrapper(t);
            }
        } finally {
            LastArguments.popCurrentArguments();
        }
    }

    public void verify() {
        behavior.verify();
    }

    public void replay() {
        throwWrappedIllegalStateException();
    }

    public void callback(Runnable runnable) {
        throwWrappedIllegalStateException();
    }

    public void checkOrder(boolean value) {
        throwWrappedIllegalStateException();
    }

    public void andReturn(Object value) {
        throwWrappedIllegalStateException();
    }

    public void andThrow(Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    public void andAnswer(IAnswer answer) {
        throwWrappedIllegalStateException();
    }
    
    public void andStubReturn(Object value) {
        throwWrappedIllegalStateException();
    }

    public void andStubThrow(Throwable throwable) {
        throwWrappedIllegalStateException();
    }
    
    public void andStubAnswer(IAnswer answer) {
        throwWrappedIllegalStateException();
    }

    public void asStub() {
        throwWrappedIllegalStateException();
    }

    public void times(Range range) {
        throwWrappedIllegalStateException();
    }

    public void setMatcher(Method method, ArgumentsMatcher matcher) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultReturnValue(Object value) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultThrowable(Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    public void setDefaultVoidCallable() {
        throwWrappedIllegalStateException();
    }

    private void throwWrappedIllegalStateException() {
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "This method must not be called in replay state."));
    }

    public void assertRecordState() {
        throwWrappedIllegalStateException();
    }
}
