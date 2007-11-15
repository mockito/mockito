/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class RecordStateInvalidRangeTest {

    MockControl<IMethods> control;

    IMethods mock;

    @Before
    public void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void setOpenCallCountTwice() {
        mock.simpleMethod();
        control.setVoidCallable(MockControl.ONE_OR_MORE);
        try {
            control.setVoidCallable(MockControl.ONE_OR_MORE);
            fail();
        } catch (IllegalStateException expected) {
            assertEquals(
                    "last method called on mock already has a non-fixed count set.",
                    expected.getMessage());
        }
    }

    @Test
    public void setIllegalMinimumCount() {
        mock.simpleMethod();
        int NEGATIVE = -1;
        try {
            control.setVoidCallable(NEGATIVE, 2);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("minimum must be >= 0", expected.getMessage());
        }
    }

    @Test
    public void setIllegalMaximumCount() {
        mock.simpleMethod();
        int NON_POSITIVE = 0;
        try {
            control.setVoidCallable(0, NON_POSITIVE);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("maximum must be >= 1", expected.getMessage());
        }
    }

    @Test
    public void setMinimumBiggerThanMaximum() {
        mock.simpleMethod();
        try {
            control.setVoidCallable(4, 3);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("minimum must be <= maximum", expected.getMessage());
        }
    }
}