/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class UsageOverloadedDefaultValueTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void overloading() {

        mock.oneArg(true);
        control.setReturnValue("true");
        control.setDefaultReturnValue("false");

        mock.oneArg((byte) 0);
        control.setReturnValue("byte 0");
        control.setDefaultReturnValue("byte 1");

        mock.oneArg((short) 0);
        control.setReturnValue("short 0");
        control.setDefaultReturnValue("short 1");

        mock.oneArg((char) 0);
        control.setReturnValue("char 0");
        control.setDefaultReturnValue("char 1");

        mock.oneArg(0);
        control.setReturnValue("int 0");
        control.setDefaultReturnValue("int 1");

        mock.oneArg((long) 0);
        control.setReturnValue("long 0");
        control.setDefaultReturnValue("long 1");

        mock.oneArg((float) 0);
        control.setReturnValue("float 0");
        control.setDefaultReturnValue("float 1");

        mock.oneArg(0.0);
        control.setReturnValue("double 0");
        control.setDefaultReturnValue("double 1");

        mock.oneArg("Object 0");
        control.setReturnValue("String 0");
        control.setDefaultReturnValue("String 1");

        control.replay();

        assertEquals("true", mock.oneArg(true));
        assertEquals("false", mock.oneArg(false));

        assertEquals("byte 0", mock.oneArg((byte) 0));
        assertEquals("byte 1", mock.oneArg((byte) 1));

        assertEquals("short 0", mock.oneArg((short) 0));
        assertEquals("short 1", mock.oneArg((short) 1));

        assertEquals("char 0", mock.oneArg((char) 0));
        assertEquals("char 1", mock.oneArg((char) 1));

        assertEquals("int 0", mock.oneArg(0));
        assertEquals("int 1", mock.oneArg(1));

        assertEquals("long 0", mock.oneArg((long) 0));
        assertEquals("long 1", mock.oneArg((long) 1));

        assertEquals("float 0", mock.oneArg((float) 0.0));
        assertEquals("float 1", mock.oneArg((float) 1.0));

        assertEquals("double 0", mock.oneArg(0.0));
        assertEquals("double 1", mock.oneArg(1.0));

        assertEquals("String 0", mock.oneArg("Object 0"));
        assertEquals("String 1", mock.oneArg("Object 1"));

        control.verify();
    }

    @Test
    public void defaultThrowable() {

        mock.oneArg("Object");
        RuntimeException expected = new RuntimeException();
        control.setDefaultThrowable(expected);

        control.replay();

        try {
            mock.oneArg("Something else");
            fail("runtime exception expected");
        } catch (RuntimeException expectedException) {
            assertSame(expected, expectedException);
        }
    }
}