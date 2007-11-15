/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.easymock.MockControl;
import org.easymock.internal.ReplayState;
import org.junit.Before;
import org.junit.Test;

public class UsageVerifyTest {
    private MockControl<IMethods> control;

    private IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void twoReturns() {
        mock.throwsNothing(true);
        control.setReturnValue("Test");
        control.setReturnValue("Test2");

        control.replay();

        assertEquals("Test", mock.throwsNothing(true));

        boolean failed = true;

        try {
            control.verify();
            failed = false;
        } catch (AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    throwsNothing(true): expected: 2, actual: 1",
                    expected.getMessage());
            assertTrue("stack trace must be filled in", Util.getStackTrace(
                    expected).indexOf(ReplayState.class.getName()) == -1);
        }

        if (!failed)
            fail("AssertionError expected");

        assertEquals("Test2", mock.throwsNothing(true));

        control.verify();

        try {
            mock.throwsNothing(true);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Unexpected method call throwsNothing(true):"
                    + "\n    throwsNothing(true): expected: 2, actual: 2 (+1)",
                    expected.getMessage());
        }
    }

    @Test
    public void atLeastTwoReturns() {
        mock.throwsNothing(true);
        control.setReturnValue("Test");
        control.setReturnValue("Test2", MockControl.ONE_OR_MORE);

        control.replay();

        assertEquals("Test", mock.throwsNothing(true));

        try {
            control.verify();
            fail("AssertionError expected");
        } catch (AssertionError expected) {

            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    throwsNothing(true): expected: at least 2, actual: 1",
                    expected.getMessage());
        }

        assertEquals("Test2", mock.throwsNothing(true));
        assertEquals("Test2", mock.throwsNothing(true));

        control.verify();
    }

    @Test
    public void twoThrows() throws IOException {
        mock.throwsIOException(0);
        control.setThrowable(new IOException());
        control.setThrowable(new IOException());
        mock.throwsIOException(1);
        control.setThrowable(new IOException());

        control.replay();

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        try {
            control.verify();
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    throwsIOException(0): expected: 2, actual: 1"
                    + "\n    throwsIOException(1): expected: 1, actual: 0",
                    expected.getMessage());
        }

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        try {
            control.verify();
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals("\n  Expectation failure on verify:"
                    + "\n    throwsIOException(1): expected: 1, actual: 0",
                    expected.getMessage());
        }

        try {
            mock.throwsIOException(1);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        control.verify();

        try {
            mock.throwsIOException(0);
            fail("AssertionError expected");
        } catch (AssertionError expected) {
            assertEquals(
                    "\n  Unexpected method call throwsIOException(0):"
                            + "\n    throwsIOException(0): expected: 2, actual: 2 (+1)",
                    expected.getMessage());
        }
    }
}
