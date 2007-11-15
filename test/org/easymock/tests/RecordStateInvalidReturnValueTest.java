/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class RecordStateInvalidReturnValueTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setInvalidBooleanReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }

    }

    @Test
    public void setInvalidLongReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue((long) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidFloatReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue((float) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDoubleReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue((double) 0);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidObjectReturnValue() {
        mock.oneArg(false);
        try {
            control.setReturnValue(new Object());
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidBooleanReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue(false, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }

    }

    @Test
    public void setInvalidLongReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue((long) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidFloatReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue((float) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidDoubleReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue((double) 0, 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setInvalidObjectReturnValueCount() {
        mock.oneArg(false);
        try {
            control.setReturnValue(new Object(), 3);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("incompatible return value type", e.getMessage());
        }
    }

    @Test
    public void setReturnValueForVoidMethod() {
        mock.simpleMethod();
        try {
            control.setReturnValue(null);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("void method cannot return a value", e.getMessage());
        }
    }
}
