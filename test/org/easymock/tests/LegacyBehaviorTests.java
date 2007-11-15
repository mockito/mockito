/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.easymock.MockControl;
import org.junit.Test;

public class LegacyBehaviorTests {

    @Test
    public void throwAfterThrowable() throws IOException {

        MockControl<IMethods> control = MockControl
                .createControl(IMethods.class);
        IMethods mock = control.getMock();

        mock.throwsIOException(0);
        control.setThrowable(new IOException());
        control.setThrowable(new IOException(), MockControl.ONE_OR_MORE);

        control.replay();

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        boolean exceptionOccured = true;
        try {
            control.verify();
            exceptionOccured = false;
        } catch (AssertionError expected) {
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    throwsIOException(0): expected: at least 2, actual: 1",
                    expected.getMessage());
        }

        if (!exceptionOccured)
            fail("exception expected");

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        control.verify();

        try {
            mock.throwsIOException(0);
            fail("IOException expected");
        } catch (IOException expected) {
        }

        control.verify();
    }
}
