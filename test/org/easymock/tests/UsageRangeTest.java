/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.easymock.MockControl;
import org.easymock.internal.Range;
import org.junit.Before;
import org.junit.Test;

public class UsageRangeTest {

    private Iterator mock;

    private MockControl<Iterator> control;

    @Before
    public void setup() {
        control = MockControl.createStrictControl(Iterator.class);
        mock = control.getMock();
    }

    @Test
    public void zeroOrMoreNoCalls() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        control.verify();
    }

    @Test
    public void zeroOrMoreOneCall() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        assertFalse(mock.hasNext());
        control.verify();
    }

    @Test
    public void zeroOrMoreThreeCalls() {
        mock.hasNext();
        control.setReturnValue(false, MockControl.ZERO_OR_MORE);
        control.replay();
        assertFalse(mock.hasNext());
        assertFalse(mock.hasNext());
        assertFalse(mock.hasNext());
        control.verify();
    }

    @Test
    public void combination() {
        mock.hasNext();
        control.setReturnValue(true, MockControl.ONE_OR_MORE);
        mock.next();
        control.setReturnValue("1");

        mock.hasNext();
        control.setReturnValue(true, MockControl.ONE_OR_MORE);
        mock.next();
        control.setReturnValue("2");

        mock.hasNext();
        control.setReturnValue(false, MockControl.ONE_OR_MORE);

        control.replay();

        assertTrue(mock.hasNext());
        assertTrue(mock.hasNext());
        assertTrue(mock.hasNext());

        assertEquals("1", mock.next());

        try {
            mock.next();
            fail();
        } catch (AssertionError expected) {
        }

        assertTrue(mock.hasNext());

        assertEquals("2", mock.next());

        assertFalse(mock.hasNext());

        control.verify();

    }

    @Test
    public void withIllegalOwnRange() {
        mock.hasNext();
        try {
            control.setReturnValue(true, new Range(2, 7));
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected Range", e.getMessage());
        }
    }
}
