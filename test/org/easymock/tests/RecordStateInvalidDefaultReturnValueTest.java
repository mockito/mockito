/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class RecordStateInvalidDefaultReturnValueTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setInvalidDefaultBooleanReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDefaultLongReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDefaultFloatReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDefaultDoubleReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidObjectDefaultReturnValue() {
        mock.oneArg(false);
        try {
            control.setDefaultReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setDefaultReturnValueWithoutMethodCall() {
        try {
            control.setDefaultReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals(
                    "method call on the mock needed before setting default return value",
                    e.getMessage());
        }
    }
}
