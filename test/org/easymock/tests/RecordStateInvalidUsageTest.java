/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class RecordStateInvalidUsageTest {

    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setReturnValueWithoutMethodCall() {
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting return value",
                    expected.getMessage());
        }
    }

    @Test
    public void setExpectedVoidCallCountWithoutMethodCall() {
        try {
            control.setVoidCallable(3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    @Test
    public void openVoidCallCountWithoutMethodCall() {
        try {
            control.setVoidCallable();
            fail("IllegalStateException expected");
        } catch (Exception expected) {
            assertEquals(
                    "method call on the mock needed before setting void callable",
                    expected.getMessage());
        }
    }

    @Test
    public void setWrongReturnValueBoolean() {
        mock.oneArg(false);
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueShort() {
        mock.oneArg(false);
        try {
            control.setReturnValue((short) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueChar() {
        mock.oneArg(false);
        try {
            control.setReturnValue((char) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueInt() {
        mock.oneArg(false);
        try {
            control.setReturnValue(0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueLong() {
        mock.oneArg(false);
        try {
            control.setReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueFloat() {
        mock.oneArg(false);
        try {
            control.setReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueDouble() {
        mock.oneArg(false);
        try {
            control.setReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }

    @Test
    public void setWrongReturnValueObject() {
        mock.oneArg(false);
        try {
            control.setReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals("incompatible return value type", expected
                    .getMessage());
        }
    }
}