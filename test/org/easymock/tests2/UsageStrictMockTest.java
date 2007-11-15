/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.internal.ReplayState;
import org.easymock.tests.IMethods;
import org.easymock.tests.Util;
import org.junit.Before;
import org.junit.Test;

public class UsageStrictMockTest {
    private IMethods mock;

    @Before
    public void setup() {
        mock = createStrictMock(IMethods.class);
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        replay(mock);
    }

    @Test
    public void orderedCallsSucces() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        verify(mock);
    }

    @Test
    public void unorderedCallsFailure() {
        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("2");
        } catch (AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            fail("unordered calls accepted");
        }
    }

    @Test
    public void tooManyCallsFailure() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        boolean failed = false;
        try {
            mock.simpleMethodWithArgument("2");
        } catch (AssertionError expected) {
            failed = true;
        }
        if (!failed) {
            fail("too many calls accepted");
        }
    }

    @Test
    public void tooFewCallsFailure() {
        mock.simpleMethodWithArgument("1");
        boolean failed = false;
        try {
            verify(mock);
        } catch (AssertionError expected) {
            failed = true;
            assertTrue("stack trace must be filled in", Util.getStackTrace(
                    expected).indexOf(ReplayState.class.getName()) == -1);
        }
        if (!failed) {
            fail("too few calls accepted");
        }
    }

    @Test
    public void differentMethods() {

        reset(mock);

        mock.booleanReturningMethod(0);
        expectLastCall().andReturn(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        expectLastCall().andReturn(false).times(2, 3);
        mock.simpleMethod();
        expectLastCall().atLeastOnce();

        replay(mock);
        assertEquals(true, mock.booleanReturningMethod(0));
        mock.simpleMethod();

        boolean failed = false;
        try {
            verify(mock);
        } catch (AssertionError expected) {
            failed = true;
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    simpleMethod(): expected: 1, actual: 1"
                            + "\n    booleanReturningMethod(1): expected: between 2 and 3, actual: 0"
                            + "\n    simpleMethod(): expected: at least 1, actual: 0",
                    expected.getMessage());
        }
        if (!failed) {
            fail("too few calls accepted");
        }

        assertEquals(false, mock.booleanReturningMethod(1));

        failed = false;
        try {
            mock.simpleMethod();
        } catch (AssertionError expected) {
            failed = true;
            assertEquals(
                    "\n  Unexpected method call simpleMethod():"
                            + "\n    booleanReturningMethod(1): expected: between 2 and 3, actual: 1",
                    expected.getMessage());
        }
        if (!failed) {
            fail("wrong call accepted");
        }
    }

    @Test
    public void range() {

        reset(mock);

        mock.booleanReturningMethod(0);
        expectLastCall().andReturn(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        expectLastCall().andReturn(false).times(2, 3);
        mock.simpleMethod();
        expectLastCall().atLeastOnce();
        expect(mock.booleanReturningMethod(1)).andReturn(false);

        replay(mock);

        mock.booleanReturningMethod(0);
        mock.simpleMethod();

        mock.booleanReturningMethod(1);
        mock.booleanReturningMethod(1);
        mock.booleanReturningMethod(1);

        boolean failed = false;

        try {
            mock.booleanReturningMethod(1);
        } catch (AssertionError expected) {
            failed = true;
            assertEquals(
                    "\n  Unexpected method call booleanReturningMethod(1):"
                            + "\n    booleanReturningMethod(1): expected: between 2 and 3, actual: 3 (+1)"
                            + "\n    simpleMethod(): expected: at least 1, actual: 0",
                    expected.getMessage());
        }
        if (!failed) {
            fail("too many calls accepted");
        }
    }

    @Test
    public void stubBehavior() {
        reset(mock);

        mock.booleanReturningMethod(1);
        expectLastCall().andReturn(true).andReturn(false).andReturn(true);
        mock.booleanReturningMethod(anyInt());
        expectLastCall().andStubReturn(true);

        replay(mock);

        assertEquals(true, mock.booleanReturningMethod(2));
        assertEquals(true, mock.booleanReturningMethod(3));
        assertEquals(true, mock.booleanReturningMethod(1));
        assertEquals(false, mock.booleanReturningMethod(1));
        assertEquals(true, mock.booleanReturningMethod(3));

        boolean failed = false;
        try {
            verify(mock);
        } catch (AssertionError expected) {
            failed = true;
            assertEquals(
                    "\n  Expectation failure on verify:"
                            + "\n    booleanReturningMethod(1): expected: 3, actual: 2",
                    expected.getMessage());
        }
        if (!failed) {
            fail("too few calls accepted");
        }
    }
}
