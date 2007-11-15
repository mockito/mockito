/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class NiceMockControlTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createNiceControl(IMethods.class);
        mock = control.getMock();
        control.replay();
    }

    @Test
    public void defaultReturnValueBoolean() {
        assertEquals(false, mock.booleanReturningMethod(12));
        control.verify();
    }

    @Test
    public void defaultReturnValueFloat() {
        assertEquals(0.0f, mock.floatReturningMethod(12), 0.0f);
        control.verify();
    }

    @Test
    public void defaultReturnValueDouble() {
        assertEquals(0.0d, mock.doubleReturningMethod(12), 0.0d);
        control.verify();
    }

    @Test
    public void defaultReturnValueObject() {
        assertEquals(null, mock.objectReturningMethod(12));
        control.verify();
    }
}
