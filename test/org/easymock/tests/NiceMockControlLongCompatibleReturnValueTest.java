/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class NiceMockControlLongCompatibleReturnValueTest {

    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createNiceControl(IMethods.class);
        mock = control.getMock();
        control.replay();
    }

    @Test
    public void byteReturningValue() {
        assertEquals((byte) 0, mock.byteReturningMethod(12));
        control.verify();
    }

    @Test
    public void shortReturningValue() {
        assertEquals((short) 0, mock.shortReturningMethod(12));
        control.verify();
    }

    @Test
    public void charReturningValue() {
        assertEquals((char) 0, mock.charReturningMethod(12));
        control.verify();
    }

    @Test
    public void intReturningValue() {
        assertEquals(0, mock.intReturningMethod(12));
        control.verify();
    }

    @Test
    public void longReturningValue() {
        assertEquals((long) 0, mock.longReturningMethod(12));
        control.verify();
    }
}