/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.junit.Test;

public class EqualsMatcherTest {
    final ArgumentsMatcher MATCHER = MockControl.EQUALS_MATCHER;

    @Test
    public void equalsMatcher() {
        assertTrue(MATCHER.matches(null, null));
        assertFalse(MATCHER.matches(null, new Object[0]));
        assertFalse(MATCHER.matches(new Object[0], null));
        assertFalse(MATCHER.matches(new Object[] { "" }, new Object[] { null }));
        assertFalse(MATCHER.matches(new Object[] { null }, new Object[] { "" }));
        assertTrue(MATCHER
                .matches(new Object[] { null }, new Object[] { null }));
        assertTrue(MATCHER.matches(new Object[] { "x" }, new Object[] { "x" }));
    }

    @Test
    public void differentNumberOfArguments() {
        assertFalse(MATCHER.matches(new Object[2], new Object[3]));
    }
}