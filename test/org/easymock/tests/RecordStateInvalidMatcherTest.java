/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class RecordStateInvalidMatcherTest {
    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setMatcherBeforeCallingMethods() {
        try {
            control.setMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "method call on the mock needed before setting matcher",
                    expected.getMessage());
        }
    }

    @Test
    public void setMatcherTwice() {
        mock.simpleMethod();
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.EQUALS_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "for method simpleMethod(), a matcher has already been set",
                    expected.getMessage());
        }
    }

    @Test
    public void setMatcherTwice2() {
        mock.simpleMethodWithArgument("");
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.EQUALS_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "for method simpleMethodWithArgument(...), a matcher has already been set",
                    expected.getMessage());
        }
    }

    @Test
    public void setSameMatcherTwice() {
        mock.simpleMethod();
        control.setMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setMatcher(MockControl.ARRAY_MATCHER);
        } catch (IllegalStateException unexpected) {
            fail("setting the same matcher should work");
        }
    }
}
