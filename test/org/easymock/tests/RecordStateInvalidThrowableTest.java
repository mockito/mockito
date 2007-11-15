/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class RecordStateInvalidThrowableTest {

    MockControl<IMethods> control;

    IMethods mock;

    private class CheckedException extends Exception {
    }

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void throwNull() {
        mock.throwsNothing(false);
        try {
            control.setThrowable(null);
            fail("NullPointerException expected");
        } catch (NullPointerException expected) {
            assertEquals("null cannot be thrown", expected.getMessage());
        }

    }

    @Test
    public void throwCheckedExceptionWhereNoCheckedExceptionIsThrown() {
        mock.throwsNothing(false);
        try {
            control.setThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + CheckedException.class.getName(), expected.getMessage());
        }
    }

    @Test
    public void throwWrongCheckedException() throws IOException {
        mock.throwsIOException(0);
        try {
            control.setThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + CheckedException.class.getName(), expected.getMessage());
        }
    }

    @Test
    public void throwAfterThrowable() throws IOException {
        mock.throwsIOException(0);
        control.setThrowable(new IOException(), MockControl.ONE_OR_MORE);
        try {
            control.setThrowable(new IOException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "last method called on mock already has a non-fixed count set.",
                    expected.getMessage());
        }
    }
}
