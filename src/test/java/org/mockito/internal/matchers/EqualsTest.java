/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.*;


public class EqualsTest extends TestBase {

    @Test
    public void shouldBeEqual() {
        assertEquals(new Equals(null), new Equals(null));
        assertEquals(new Equals(new Integer(2)), new Equals(new Integer(2)));
        assertFalse(new Equals(null).equals(null));
        assertFalse(new Equals(null).equals("Test"));
        assertEquals(1, new Equals(null).hashCode());
    }

    @Test
    public void shouldArraysBeEqual() {
        assertTrue(new Equals(new int[] {1, 2}).matches(new int[] {1, 2}));
        assertFalse(new Equals(new Object[] {"1"}).matches(new Object[] {"1.0"}));
    }

    @Test
    public void shouldDescribeWithExtraTypeInfo() throws Exception {
        String descStr = new Equals(100).toStringWithType();

        assertEquals("(Integer) 100", descStr);
    }

    @Test
    public void shouldDescribeWithExtraTypeInfoOfLong() throws Exception {
        String descStr = new Equals(100L).toStringWithType();

        assertEquals("(Long) 100L", descStr);
    }

    @Test
    public void shouldDescribeWithTypeOfString() throws Exception {
        String descStr = new Equals("x").toStringWithType();

        assertEquals("(String) \"x\"", descStr);
    }

    @Test
    public void shouldAppendQuotingForString() {
        String descStr = new Equals("str").toString();

        assertEquals("\"str\"", descStr);
    }

    @Test
    public void shouldAppendQuotingForChar() {
        String descStr = new Equals('s').toString();

        assertEquals("'s'", descStr);
    }

    @Test
    public void shouldDescribeUsingToString() {
        String descStr = new Equals(100).toString();

        assertEquals("100", descStr);
    }

    @Test
    public void shouldDescribeNull() {
        String descStr = new Equals(null).toString();

        assertEquals("null", descStr);
    }

    @Test
    public void shouldMatchTypes() throws Exception {
        //when
        ContainsExtraTypeInfo equals = new Equals(10);

        //then
        assertTrue(equals.typeMatches(10));
        assertFalse(equals.typeMatches(10L));
    }

    @Test
    public void shouldMatchTypesSafelyWhenActualIsNull() throws Exception {
        //when
        ContainsExtraTypeInfo equals = new Equals(null);

        //then
        assertFalse(equals.typeMatches(10));
    }

    @Test
    public void shouldMatchTypesSafelyWhenGivenIsNull() throws Exception {
        //when
        ContainsExtraTypeInfo equals = new Equals(10);

        //then
        assertFalse(equals.typeMatches(null));
    }
}
