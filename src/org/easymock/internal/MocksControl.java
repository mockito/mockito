/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import org.easymock.ArgumentsMatcher;
import org.easymock.IAnswer;
import org.easymock.IExpectationSetters;
import org.easymock.IMocksControl;

public class MocksControl implements IMocksControl {

    private IMocksControlState state;

    private IMocksBehavior behavior;
    
    public enum MockType {
        NICE, DEFAULT, STRICT
    }

    private MockType type;

    public MocksControl(MockType type) {
        this.type = type;
        reset();
    }

    public IMocksControlState getState() {
        return state;
    }

    public <T> T createMock(Class<T> toMock) {
        try {
            state.assertRecordState();
            ClassProxyFactory<T> proxyFactory = new ClassProxyFactory<T>();
            return proxyFactory.createProxy(toMock, new MockitoObjectMethodsFilter(
                    toMock, new MockInvocationHandler(this), null));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public <T> T createMock(String name, Class<T> toMock) {
        try {
            state.assertRecordState();
            ClassProxyFactory<T> proxyFactory = new ClassProxyFactory<T>();
            return proxyFactory.createProxy(toMock, new MockitoObjectMethodsFilter(
                    toMock, new MockInvocationHandler(this), name));
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public final void reset() {
        behavior = new MocksBehavior(type == MockType.NICE);
        behavior.checkOrder(type == MockType.STRICT);
        state = new RecordState(behavior);
        LastControl.reportLastControl(null);
    }

    public void replay() {
        try {
            state.replay();
            state = new ReplayState(behavior);
            LastControl.reportLastControl(null);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void verify() {
        try {
            state.verify();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        } catch (AssertionErrorWrapper e) {
            throw (AssertionError) e.getAssertionError().fillInStackTrace();
        }
    }

    public void checkOrder(boolean value) {
        try {
            state.checkOrder(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    // methods from IBehaviorSetters

    public IExpectationSetters andReturn(Object value) {
        try {
            state.andReturn(value);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters andThrow(Throwable throwable) {
        try {
            state.andThrow(throwable);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters andAnswer(IAnswer answer) {
        try {
            state.andAnswer(answer);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubReturn(Object value) {
        try {
            state.andStubReturn(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubThrow(Throwable throwable) {
        try {
            state.andStubThrow(throwable);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void andStubAnswer(IAnswer answer) {
        try {
            state.andStubAnswer(answer);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void asStub() {
        try {
            state.asStub();
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters times(int times) {
        try {
            state.times(new Range(times));
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters times(int min, int max) {
        try {
            state.times(new Range(min, max));
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters once() {
        try {
            state.times(ONCE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters atLeastOnce() {
        try {
            state.times(AT_LEAST_ONCE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public IExpectationSetters anyTimes() {
        try {
            state.times(ZERO_OR_MORE);
            return this;
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    /**
     * Exactly one call.
     */
    public static final Range ONCE = new Range(1);

    /**
     * One or more calls.
     */
    public static final Range AT_LEAST_ONCE = new Range(1, Integer.MAX_VALUE);

    /**
     * Zero or more calls.
     */
    public static final Range ZERO_OR_MORE = new Range(0, Integer.MAX_VALUE);

    public void setLegacyDefaultMatcher(ArgumentsMatcher matcher) {
        try {
            state.setDefaultMatcher(matcher);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void setLegacyMatcher(ArgumentsMatcher matcher) {
        try {
            state.setMatcher(null, matcher);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void setLegacyDefaultReturnValue(Object value) {
        try {
            state.setDefaultReturnValue(value);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }

    public void setLegacyDefaultVoidCallable() {
        state.setDefaultVoidCallable();
    }

    public void setLegacyDefaultThrowable(Throwable throwable) {
        try {
            state.setDefaultThrowable(throwable);
        } catch (RuntimeExceptionWrapper e) {
            throw (RuntimeException) e.getRuntimeException().fillInStackTrace();
        }
    }
}
