/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class DefaultMatcherTest {

    public static interface ArrayInterface {
        void methodA(int[] argument);

        void methodB(int[] argument);
    }

    private MockControl<ArrayInterface> control;

    private ArrayInterface mock;

    @Before
    public void setup() {
        control = MockControl.createControl(ArrayInterface.class);
        mock = control.getMock();
    }

    @Test
    public void defaultMatcher() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);

        mock.methodA(new int[] { 1, 1 });
        mock.methodB(new int[] { 2, 2 });

        control.replay();

        mock.methodA(new int[] { 1, 1 });
        mock.methodB(new int[] { 2, 2 });

        control.verify();
    }

    @Test
    public void failInReplayState() {
        control.replay();
        try {
            control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void failIfDefaultMatcherSetTwice() {
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        try {
            control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "default matcher can only be set once directly after creation of the MockControl",
                    expected.getMessage());
        }
    }

    @Test
    public void defaultMatcherSetTooLate() {
        int[] integers = new int[] { 1, 1 };
        int[] integers2 = new int[] { 2, 2 };
        mock.methodA(integers);
        control.setVoidCallable();
        control.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        mock.methodA(integers2);
        control.setVoidCallable();
        control.replay();

        boolean failed = true;
        try {
            mock.methodA(new int[] { 1, 1 });
            failed = false;
        } catch (AssertionError expected) {
        }
        if (!failed) {
            fail();
        }
        mock.methodA(integers);
        mock.methodA(new int[] { 2, 2 });
        control.verify();
    }
}
