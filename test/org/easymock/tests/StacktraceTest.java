/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class StacktraceTest {

    private MockControl<IMethods> control;

    private IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createStrictControl(IMethods.class);
        mock = control.getMock();
    }

    private static class ToStringThrowsException {
        @Override
        public String toString() {
            throw new NullPointerException();
        }
    }

    @Test
    public void assertRecordStateNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        mock.oneArg(new ToStringThrowsException());
        try {
            mock.oneArg(new ToStringThrowsException());
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.getStackTrace(expected).indexOf(
                            ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertReplayNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        mock.oneArg(new ToStringThrowsException());
        try {
            control.replay();
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.getStackTrace(expected).indexOf(
                            ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertReplayStateNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        control.replay();
        try {
            mock.oneArg(new ToStringThrowsException());
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.getStackTrace(expected).indexOf(
                            ToStringThrowsException.class.getName()) > 0);
        }
    }

    @Test
    public void assertVerifyNoFillInStacktraceWhenExceptionNotFromEasyMock() {
        mock.oneArg(new ToStringThrowsException());
        control.setReturnValue("");
        control.replay();
        try {
            control.verify();
            fail();
        } catch (NullPointerException expected) {
            assertTrue("stack trace must not be cut",
                    Util.getStackTrace(expected).indexOf(
                            ToStringThrowsException.class.getName()) > 0);
        }
    }
}
