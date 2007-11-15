/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import org.easymock.MockControl;
import org.easymock.internal.MocksBehavior;
import org.easymock.internal.ReplayState;
import org.easymock.internal.RuntimeExceptionWrapper;
import org.junit.Before;
import org.junit.Test;

public class ReplayStateInvalidCallsTest {

    private ReplayState control;

    private Exception exception;

    @Before
    public void setUp() {
        exception = new Exception();
        control = new ReplayState(new MocksBehavior(false));
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void expectAndThrowLongWithMinMax() {
        control.andThrow(exception);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void expectAndReturnObjectWithMinMax() {
        control.andReturn("");
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void setDefaultMatcher() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void asStub() {
        control.asStub();
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void setMatcher() {
        control.setMatcher(null, MockControl.ARRAY_MATCHER);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void setDefaultReturnValue() {
        control.setDefaultReturnValue("");
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void setDefaultThrowable() {
        control.setDefaultThrowable(exception);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void setDefaultVoidCallable() {
        control.setDefaultVoidCallable();
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void replay() {
        control.replay();
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void checkOrder() {
        control.checkOrder(true);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andStubReturn() {
        control.andStubReturn("7");
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andStubThrow() {
        control.andStubThrow(new RuntimeException());
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andStubAnswer() {
        control.andStubAnswer(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void times() {
        control.times(MockControl.ONE);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void callback() {
        control.callback(new Runnable() {
            public void run() {
            };
        });
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andReturn() {
        control.andReturn(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andThrow() {
        control.andThrow(new RuntimeException());
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void andAnswer() {
        control.andAnswer(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void defaultThrowable() {
        control.setDefaultThrowable(new RuntimeException());
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void defaultReturnValue() {
        control.setDefaultReturnValue(null);
    }

    @Test(expected = RuntimeExceptionWrapper.class)
    public void defaultVoidCallable() {
        control.setDefaultVoidCallable();
    }
}