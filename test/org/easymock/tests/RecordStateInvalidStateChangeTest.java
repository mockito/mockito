/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.easymock.internal.RecordState;
import org.junit.Before;
import org.junit.Test;

public class RecordStateInvalidStateChangeTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void activateWithoutReturnValue() {
        mock.oneArg(false);
        try {
            control.replay();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals(
                    "missing behavior definition for the preceeding method call oneArg(false)",
                    e.getMessage());
            assertTrue("stack trace must be cut", Util.getStackTrace(e)
                    .indexOf(RecordState.class.getName()) == -1);
        }
    }

    @Test
    public void secondCallWithoutReturnValue() {
        mock.oneArg(false);
        try {
            mock.oneArg(false);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals(
                    "missing behavior definition for the preceeding method call oneArg(false)",
                    e.getMessage());
            assertTrue("stack trace must be cut", Util.getStackTrace(e)
                    .indexOf(RecordState.class.getName()) == -1);
        }
    }

    @Test
    public void verifyWithoutActivation() {
        try {
            control.verify();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("calling verify is not allowed in record state", e
                    .getMessage());
        }
    }
}
