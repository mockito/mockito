/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class EqualsTest extends TestBase {

    @Mock private IMethods mock;

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
        String descStr = new Equals(100).toStringWithType(Integer.class.getSimpleName());

        assertEquals("(Integer) 100", descStr);
    }

    @Test
    public void shouldDescribeWithExtraTypeInfoOfLong() throws Exception {
        String descStr = new Equals(100L).toStringWithType(Long.class.getSimpleName());

        assertEquals("(Long) 100L", descStr);
    }

    @Test
    public void shouldDescribeWithTypeOfString() throws Exception {
        String descStr = new Equals("x").toStringWithType(String.class.getSimpleName());

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
        // when
        ContainsExtraTypeInfo equals = new Equals(10);

        // then
        assertTrue(equals.typeMatches(10));
        assertFalse(equals.typeMatches(10L));
    }

    @Test
    public void shouldMatchTypesSafelyWhenActualIsNull() throws Exception {
        // when
        ContainsExtraTypeInfo equals = new Equals(null);

        // then
        assertFalse(equals.typeMatches(10));
    }

    @Test
    public void shouldMatchTypesSafelyWhenGivenIsNull() throws Exception {
        // when
        ContainsExtraTypeInfo equals = new Equals(10);

        // then
        assertFalse(equals.typeMatches(null));
    }

    @Test
    public void shouldMockVarargInvocation() {
        given(mock.varargs(eq("one param"))).willReturn(1);

        assertThat(mock.varargs("one param")).isEqualTo(1);
        assertThat(mock.varargs()).isEqualTo(0);
        assertThat(mock.varargs("different")).isEqualTo(0);
        assertThat(mock.varargs("one param", "another")).isEqualTo(0);
    }

    @Test
    public void shouldVerifyInvocation() {
        mock.varargs("one param");

        verify(mock).varargs(eq("one param"));
        verify(mock, never()).varargs();
        verify(mock, never()).varargs(eq("different"));
        verify(mock, never()).varargs(eq("one param"), eq("another"));
    }

    @Test
    public void shouldMockVarargInvocation_raw() {
        given(mock.varargs(eq(new String[] {"one param"}))).willReturn(1);

        assertThat(mock.varargs("one param")).isEqualTo(1);
        assertThat(mock.varargs()).isEqualTo(0);
        assertThat(mock.varargs("different")).isEqualTo(0);
        assertThat(mock.varargs("one param", "another")).isEqualTo(0);
    }

    @Test
    public void shouldVerifyInvocation_raw() {
        mock.varargs("one param");

        verify(mock).varargs(eq(new String[] {"one param"}));
        verify(mock, never()).varargs(eq(new String[] {}));
        verify(mock, never()).varargs(eq(new String[] {"different"}));
        verify(mock, never()).varargs(eq(new String[] {"one param", "another"}));
    }
}
