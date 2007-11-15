/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.easymock.MockControl;
import org.easymock.internal.ReplayState;
import org.junit.Before;
import org.junit.Test;

public class ReplayStateInvalidUsageTest {

    private MockControl<IMethods> control;

    private Exception exception;

    private ReplayState replayState;

    private IMocksControl mocksControl;

    @Before
    public void setUp() {
        exception = new Exception();
        control = MockControl.createControl(IMethods.class);
        control.replay();
        mocksControl = EasyMock.createControl();
        mocksControl.replay();
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowObjectWithMinMax() {
        control.expectAndThrow("", exception, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowDoubleWithMinMax() {
        control.expectAndThrow(0.0d, exception, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowFloatWithMinMax() {
        control.expectAndThrow(0.0f, exception, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowLongWithMinMax() {
        control.expectAndThrow(0, exception, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowBooleanWithMinMax() {
        control.expectAndThrow(false, exception, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowObjectWithCount() {
        control.expectAndThrow("", exception, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowDoubleWithCount() {
        control.expectAndThrow(0.0d, exception, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowFloatWithCount() {
        control.expectAndThrow(0.0f, exception, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowLongWithCount() {
        control.expectAndThrow(0, exception, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowBooleanWithCount() {
        control.expectAndThrow(false, exception, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowObjectWithRange() {
        control.expectAndThrow("", exception, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowDoubleWithRange() {
        control.expectAndThrow(0.0d, exception, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowFloatWithRange() {
        control.expectAndThrow(0.0f, exception, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowLongWithRange() {
        control.expectAndThrow(0, exception, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowBooleanWithRange() {
        control.expectAndThrow(false, exception, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowObject() {
        control.expectAndThrow("", exception);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowDouble() {
        control.expectAndThrow(0.0d, exception);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowFloat() {
        control.expectAndThrow(0.0f, exception);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowLong() {
        control.expectAndThrow(0, exception);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndThrowBoolean() {
        control.expectAndThrow(false, exception);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnObjectWithMinMax() {
        control.expectAndReturn("", "", 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnDoubleWithMinMax() {
        control.expectAndReturn(0.0d, 0.0d, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnFloatWithMinMax() {
        control.expectAndReturn(0.0f, 0.0f, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnLongWithMinMax() {
        control.expectAndReturn(0, 0, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnBooleanWithMinMax() {
        control.expectAndReturn(false, false, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnObjectWithCount() {
        control.expectAndReturn("", "", 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnDoubleWithCount() {
        control.expectAndReturn(0.0d, 0.0d, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnFloatWithCount() {
        control.expectAndReturn(0.0f, 0.0f, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnLongWithCount() {
        control.expectAndReturn(0, 0, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnBooleanWithCount() {
        control.expectAndReturn(false, false, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnObjectWithRange() {
        control.expectAndReturn("", "", MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnDoubleWithRange() {
        control.expectAndReturn(0.0d, 0.0d, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnFloatWithRange() {
        control.expectAndReturn(0.0f, 0.0f, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnLongWithRange() {
        control.expectAndReturn(0, 0, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnBooleanWithRange() {
        control.expectAndReturn(false, false, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnObject() {
        control.expectAndReturn("", "");
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnDouble() {
        control.expectAndReturn(0.0d, 0.0d);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnFloat() {
        control.expectAndReturn(0.0f, 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnLong() {
        control.expectAndReturn(0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void expectAndReturnBoolean() {
        control.expectAndReturn(false, false);
    }

    @Test(expected = IllegalStateException.class)
    public void setDefaultMatcher() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueObjectWithMinMax() {
        control.setReturnValue("", 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueDoubleWithMinMax() {
        control.setReturnValue(0.0d, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueFloatWithMinMax() {
        control.setReturnValue(0.0f, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueLongWithMinMax() {
        control.setReturnValue(0, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueBooleanWithMinMax() {
        control.setReturnValue(false, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void setThrowableWithMinMax() {
        control.setThrowable(exception, 1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void setVoidCallableWithMinMax() {
        control.setVoidCallable(1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void setMatcher() {
        control.setMatcher(MockControl.ARRAY_MATCHER);
    }

    @Test(expected = IllegalStateException.class)
    public void setDefaultReturnValueObject() {
        control.setDefaultReturnValue("");
    }

    @Test(expected = IllegalStateException.class)
    public void setDefaultReturnValueDouble() {
        control.setDefaultReturnValue(0.0d);
    }

    @Test(expected = IllegalStateException.class)
    public void setDefaultReturnValueFloat() {
        control.setDefaultReturnValue(0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void setDefaultReturnValueLong() {
        control.setDefaultReturnValue(0);
    }

    @Test(expected = IllegalStateException.class)
    public void setDefaultReturnValueBoolean() {
        control.setDefaultReturnValue(false);
    }

    @Test(expected = IllegalStateException.class)
    public void setDefaultThrowable() {
        control.setDefaultThrowable(exception);
    }

    @Test(expected = IllegalStateException.class)
    public void setDefaultVoidCallable() {
        control.setDefaultVoidCallable();
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueObjectWithRange() {
        control.setReturnValue("", MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueLongWithRange() {
        control.setReturnValue(0, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueFloatWithRange() {
        control.setReturnValue(0.0f, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueDoubleWithRange() {
        control.setReturnValue(0.0d, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueBooleanWithRange() {
        control.setReturnValue(false, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void setThrowableWithRange() {
        control.setThrowable(exception, MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void setVoidCallableWithRange() {
        control.setVoidCallable(MockControl.ONE);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueObjectWithCount() {
        control.setReturnValue("", 1);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueLongWithCount() {
        control.setReturnValue(0, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueFloatWithCount() {
        control.setReturnValue(0.0f, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueDoubleWithCount() {
        control.setReturnValue(0.0d, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueBooleanWithCount() {
        control.setReturnValue(false, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void setThrowableWithCount() {
        control.setThrowable(exception, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void setVoidCallableWithCount() {
        control.setVoidCallable(1);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueObject() {
        control.setReturnValue("");
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueDouble() {
        control.setReturnValue(0.0d);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueFloat() {
        control.setReturnValue(0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueLong() {
        control.setReturnValue(0);
    }

    @Test(expected = IllegalStateException.class)
    public void setReturnValueBoolean() {
        control.setReturnValue(false);
    }

    @Test(expected = IllegalStateException.class)
    public void setThrowable() {
        control.setThrowable(exception);
    }

    @Test(expected = IllegalStateException.class)
    public void setVoidCallable() {
        control.setVoidCallable();
    }

    @Test(expected = IllegalStateException.class)
    public void replay() {
        control.replay();
    }

    @Test(expected = IllegalStateException.class)
    public void createMock() {
        mocksControl.createMock(IMethods.class);
    }

    @Test(expected = IllegalStateException.class)
    public void createMockWithName() {
        mocksControl.createMock("", IMethods.class);
    }

    @Test(expected = IllegalStateException.class)
    public void checkOrder() {
        mocksControl.checkOrder(true);
    }

    @Test(expected = IllegalStateException.class)
    public void andStubReturn() {
        mocksControl.andStubReturn("7");
    }

    @Test(expected = IllegalStateException.class)
    public void andStubThrow() {
        mocksControl.andStubThrow(new RuntimeException());
    }

    @Test(expected = IllegalStateException.class)
    public void asStub() {
        mocksControl.asStub();
    }

    @Test(expected = IllegalStateException.class)
    public void times() {
        mocksControl.times(3);
    }

    @Test(expected = IllegalStateException.class)
    public void anyTimes() {
        mocksControl.anyTimes();
    }
}