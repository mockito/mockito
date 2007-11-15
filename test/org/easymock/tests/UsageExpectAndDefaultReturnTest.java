/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

/**
 * Same as UsageExpectAndReturnTest except that each mocked method is called
 * twice to make sure the defaulting works fine.
 * 
 * @author Henri Tremblay
 */
public class UsageExpectAndDefaultReturnTest {
    private MockControl<IMethods> control;

    private IMethods mock;

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void booleanType() {
        control.expectAndDefaultReturn(mock.booleanReturningMethod(4), true);
        control.replay();
        assertEquals(true, mock.booleanReturningMethod(4));
        assertEquals(true, mock.booleanReturningMethod(4));
        control.verify();
    }

    @Test
    public void longType() {
        control.expectAndDefaultReturn(mock.longReturningMethod(4), 12l);
        control.replay();
        assertEquals(12l, mock.longReturningMethod(4));
        assertEquals(12l, mock.longReturningMethod(4));
        control.verify();
    }

    @Test
    public void floatType() {
        control.expectAndDefaultReturn(mock.floatReturningMethod(4), 12f);
        control.replay();
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        assertEquals(12f, mock.floatReturningMethod(4), 0f);
        control.verify();
    }

    @Test
    public void doubleType() {
        control.expectAndDefaultReturn(mock.doubleReturningMethod(4), 12.0);
        control.replay();
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        assertEquals(12.0, mock.doubleReturningMethod(4), 0.0);
        control.verify();
    }

    @Test
    public void objectType() {
        control.expectAndDefaultReturn(mock.objectReturningMethod(4), "12");
        control.replay();
        assertEquals("12", mock.objectReturningMethod(4));
        assertEquals("12", mock.objectReturningMethod(4));
        control.verify();
    }

}
