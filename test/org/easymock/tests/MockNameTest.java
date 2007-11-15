/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.*;
import org.junit.*;

public class MockNameTest {

    private MockControl<IMethods> control;

    @Test
    public void defaultName() {
        control = MockControl.createControl(IMethods.class);
        String expected = "EasyMock for " + IMethods.class.toString();
        String actual = control.getMock().toString();
        assertEquals(expected, actual);
    }
}
