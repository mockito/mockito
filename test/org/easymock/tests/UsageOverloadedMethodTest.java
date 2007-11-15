/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class UsageOverloadedMethodTest {

    MockControl<IMethods> controller;

    IMethods mock;

    @Before
    public void setup() {
        controller = MockControl.createControl(IMethods.class);
        mock = controller.getMock();
    }

    @Test
    public void overloading() {

        mock.oneArg(true);
        controller.setReturnValue("true");
        mock.oneArg(false);
        controller.setReturnValue("false");

        mock.oneArg((byte) 0);
        controller.setReturnValue("byte 0");
        mock.oneArg((byte) 1);
        controller.setReturnValue("byte 1");

        mock.oneArg((short) 0);
        controller.setReturnValue("short 0");
        mock.oneArg((short) 1);
        controller.setReturnValue("short 1");

        mock.oneArg((char) 0);
        controller.setReturnValue("char 0");
        mock.oneArg((char) 1);
        controller.setReturnValue("char 1");

        mock.oneArg(0);
        controller.setReturnValue("int 0");
        mock.oneArg(1);
        controller.setReturnValue("int 1");

        mock.oneArg((long) 0);
        controller.setReturnValue("long 0");
        mock.oneArg((long) 1);
        controller.setReturnValue("long 1");

        mock.oneArg((float) 0);
        controller.setReturnValue("float 0");
        mock.oneArg((float) 1);
        controller.setReturnValue("float 1");

        mock.oneArg(0.0);
        controller.setReturnValue("double 0");
        mock.oneArg(1.0);
        controller.setReturnValue("double 1");

        mock.oneArg("Object 0");
        controller.setReturnValue("1");
        mock.oneArg("Object 1");
        controller.setReturnValue("2");

        controller.replay();

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

        assertEquals("double 1", mock.oneArg(1.0));
        assertEquals("double 0", mock.oneArg(0.0));

        assertEquals("1", mock.oneArg("Object 0"));
        assertEquals("2", mock.oneArg("Object 1"));

        controller.verify();
    }

    @Test
    public void nullReturnValue() {

        mock.oneArg("Object");
        controller.setReturnValue(null);

        controller.replay();

        assertNull(mock.oneArg("Object"));

    }

    @Test
    public void moreThanOneResultAndOpenCallCount() {
        mock.oneArg(true);
        controller.setReturnValue("First Result", 4);
        controller.setReturnValue("Second Result", 2);
        controller.setThrowable(new RuntimeException("Third Result"), 3);
        controller.setReturnValue("Following Result", MockControl.ONE_OR_MORE);

        controller.replay();

        assertEquals("First Result", mock.oneArg(true));
        assertEquals("First Result", mock.oneArg(true));
        assertEquals("First Result", mock.oneArg(true));
        assertEquals("First Result", mock.oneArg(true));

        assertEquals("Second Result", mock.oneArg(true));
        assertEquals("Second Result", mock.oneArg(true));

        try {
            mock.oneArg(true);
            fail("expected exception");
        } catch (RuntimeException expected) {
            assertEquals("Third Result", expected.getMessage());
        }

        try {
            mock.oneArg(true);
            fail("expected exception");
        } catch (RuntimeException expected) {
            assertEquals("Third Result", expected.getMessage());
        }

        try {
            mock.oneArg(true);
            fail("expected exception");
        } catch (RuntimeException expected) {
            assertEquals("Third Result", expected.getMessage());
        }

        assertEquals("Following Result", mock.oneArg(true));
        assertEquals("Following Result", mock.oneArg(true));
        assertEquals("Following Result", mock.oneArg(true));
        assertEquals("Following Result", mock.oneArg(true));
        assertEquals("Following Result", mock.oneArg(true));

        controller.verify();
    }
}