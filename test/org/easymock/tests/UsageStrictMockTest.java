/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.easymock.internal.ReplayState;
import org.junit.Before;
import org.junit.Test;

public class UsageStrictMockTest {
    private MockControl<IMethods> control;

    private IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createStrictControl(IMethods.class);
        mock = control.getMock();

        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        control.replay();
    }

    @Test
    public void verify() {
        control.reset();
        control.replay();
        control.verify();
    }

    @Test
    public void orderedCallsSucces() {
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        control.verify();
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
            control.verify();
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

        control.reset();

        mock.booleanReturningMethod(0);
        control.setReturnValue(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        control.setReturnValue(false, 2, 3);
        mock.simpleMethod();
        control.setVoidCallable(MockControl.ONE_OR_MORE);

        control.replay();
        assertEquals(true, mock.booleanReturningMethod(0));
        mock.simpleMethod();

        boolean failed = false;
        try {
            control.verify();
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

        control.reset();

        mock.booleanReturningMethod(0);
        control.setReturnValue(true);
        mock.simpleMethod();
        mock.booleanReturningMethod(1);
        control.setReturnValue(false, 2, 3);
        mock.simpleMethod();
        control.setVoidCallable(MockControl.ONE_OR_MORE);
        mock.booleanReturningMethod(1);
        control.setReturnValue(false);

        control.replay();

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
    public void defaultBehavior() {
        control.reset();

        mock.booleanReturningMethod(1);
        control.setReturnValue(true);
        control.setReturnValue(false);
        control.setReturnValue(true);
        control.setDefaultReturnValue(true);

        control.replay();

        assertEquals(true, mock.booleanReturningMethod(2));
        assertEquals(true, mock.booleanReturningMethod(3));
        assertEquals(true, mock.booleanReturningMethod(1));
        assertEquals(false, mock.booleanReturningMethod(1));
        assertEquals(true, mock.booleanReturningMethod(3));

        boolean failed = false;
        try {
            control.verify();
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

    @Test
    public void unexpectedCallWithArray() {
        control.reset();
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        mock.arrayMethod(new String[] { "Test", "Test 2" });
        control.replay();
        boolean failed = false;
        String[] strings = new String[] { "Test" };
        try {
            mock.arrayMethod(strings);
        } catch (AssertionError expected) {
            failed = true;
            assertEquals(
                    "\n  Unexpected method call arrayMethod("
                            + strings.toString()
                            + "):"
                            + "\n    arrayMethod([\"Test\", \"Test 2\"]): expected: 1, actual: 0",
                    expected.getMessage());
        }
        if (!failed) {
            fail("exception expected");
        }

    }
}
