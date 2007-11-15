/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.junit.Test;

public class ArrayMatcherTest {

    private static ArgumentsMatcher MATCHER = MockControl.ARRAY_MATCHER;

    @Test
    public void booleanArray() {
        assertTrue(equals(new boolean[] { true }, new boolean[] { true }));
        assertFalse(equals(new boolean[] { true }, new boolean[] { false }));
    }

    @Test
    public void byteArray() {
        assertTrue(equals(new byte[] { 6 }, new byte[] { 6 }));
        assertFalse(equals(new byte[] { 6 }, new byte[] { 7 }));
    }

    @Test
    public void charArray() {
        assertTrue(equals(new char[] { 'x' }, new char[] { 'x' }));
        assertFalse(equals(new char[] { 'x' }, new char[] { 'y' }));
    }

    @Test
    public void doubleArray() {
        assertTrue(equals(new double[] { 6.0 }, new double[] { 6.0 }));
        assertFalse(equals(new double[] { 6.0 }, new double[] { 7.0 }));
    }

    @Test
    public void floatArray() {
        assertTrue(equals(new float[] { 6.0F }, new float[] { 6.0F }));
        assertFalse(equals(new float[] { 6.0F }, new float[] { 7.0F }));
    }

    @Test
    public void intArray() {
        assertTrue(equals(new int[] { 6 }, new int[] { 6 }));
        assertFalse(equals(new int[] { 6 }, new int[] { 7 }));
    }

    @Test
    public void longArray() {
        assertTrue(equals(new long[] { 6 }, new long[] { 6 }));
        assertFalse(equals(new long[] { 6 }, new long[] { 7 }));
    }

    @Test
    public void shortArray() {
        assertTrue(equals(new short[] { 6 }, new short[] { 6 }));
        assertFalse(equals(new short[] { 6 }, new short[] { 7 }));
    }

    @Test
    public void objectArray() {
        assertTrue(equals(new String[] { "1", "2" }, new String[] { "1", "2" }));
        assertFalse(equals(new String[] { "1", "2" }, new String[] { "2", "2" }));
    }

    @Test
    public void nonArray() {
        assertTrue(equals("1", "1"));
        assertFalse(equals("1", "2"));
    }

    @Test
    public void testToString() {
        assertEquals("[true, false]", stringFor(new boolean[] { true, false }));
        assertEquals("[6, 7]", stringFor(new byte[] { 6, 7 }));
        assertEquals("['x', 'y']", stringFor(new char[] { 'x', 'y' }));
        assertEquals("[6.0, 7.0]", stringFor(new double[] { 6, 7 }));
        assertEquals("[6.0, 7.0]", stringFor(new float[] { 6, 7 }));
        assertEquals("[6, 7]", stringFor(new int[] { 6, 7 }));
        assertEquals("[6, 7]", stringFor(new long[] { 6, 7 }));
        assertEquals("[6, 7]", stringFor(new short[] { 6, 7 }));
        assertEquals("[\"1\", \"2\"]", stringFor(new String[] { "1", "2" }));
        assertEquals("[\"1\", \"2\"]", stringFor(new Object[] { "1", "2" }));
    }

    @Test
    public void toStringMixed() {
        assertEquals("3, [\"1\", 2.0], \"Test\"", MATCHER
                .toString(new Object[] { new Integer(3),
                        new Object[] { "1", new Float(2.0) }, "Test" }));
    }

    private String stringFor(Object argument) {
        return MATCHER.toString(new Object[] { argument });
    }

    private boolean equals(Object o1, Object o2) {
        Object[] expected = new Object[] { o1 };
        Object[] actual = new Object[] { o2 };
        return MATCHER.matches(expected, actual);
    }
}
