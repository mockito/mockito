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

public class RecordStateInvalidDefaultThrowableTest {
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
            control.setDefaultThrowable(null);
            fail("NullPointerException expected");
        } catch (NullPointerException expected) {
            assertEquals("null cannot be thrown", expected.getMessage());
        }

    }

    @Test
    public void throwCheckedExceptionWhereNoCheckedExceptionIsThrown() {
        mock.throwsNothing(false);
        try {
            control.setDefaultThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + this.getClass().getName() + "$CheckedException", expected
                    .getMessage());
        }
    }

    @Test
    public void throwWrongCheckedException() throws IOException {
        mock.throwsIOException(0);
        try {
            control.setDefaultThrowable(new CheckedException());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("last method called on mock cannot throw "
                    + this.getClass().getName() + "$CheckedException", expected
                    .getMessage());
        }
    }

    @Test
    public void setDefaultThrowableWithoutMethodCall() throws IOException {
        try {
            control.setDefaultThrowable(new RuntimeException());
            fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting default Throwable",
                    expected.getMessage());
        }
    }

}
