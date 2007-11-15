/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

public class NiceMockTest {

    IMethods mock;

    @Before
    public void setup() {
        mock = createNiceMock(IMethods.class);
        replay(mock);
    }

    @Test
    public void defaultReturnValueBoolean() {
        assertEquals(false, mock.booleanReturningMethod(12));
        verify(mock);
    }

    @Test
    public void defaultReturnValueFloat() {
        assertEquals(0.0f, mock.floatReturningMethod(12), 0.0f);
        verify(mock);
    }

    @Test
    public void defaultReturnValueDouble() {
        assertEquals(0.0d, mock.doubleReturningMethod(12), 0.0d);
        verify(mock);
    }

    @Test
    public void defaultReturnValueObject() {
        assertEquals(null, mock.objectReturningMethod(12));
        verify(mock);
    }
}
