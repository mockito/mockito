/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.*;
import static org.mockito.CrazyMatchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.VerificationError;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")  
public class MatchersTest {
    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }
    
    @Test
    public void andOverloaded() {
        stub(mock.oneArg(and(eq(false), eq(false)))).andReturn("0");
        stub(mock.oneArg(and(eq((byte) 1), eq((byte) 1)))).andReturn("1");
        stub(mock.oneArg(and(eq('a'), eq('a')))).andReturn("2");
        stub(mock.oneArg(and(eq((double) 1), eq((double) 1)))).andReturn("3");
        stub(mock.oneArg(and(eq((float) 1), eq((float) 1)))).andReturn("4");
        stub(mock.oneArg(and(eq((int) 1), eq((int) 1)))).andReturn("5");
        stub(mock.oneArg(and(eq((long) 1), eq((long) 1)))).andReturn("6");
        stub(mock.oneArg(and(eq((short) 1), eq((short) 1)))).andReturn("7");
        stub(mock.oneArg(and(contains("a"), contains("d")))).andReturn("8");
        stub(mock.oneArg(and(isA(Class.class), eq(Object.class)))).andReturn("9");

        assertEquals("0", mock.oneArg(false));
        assertEquals(null, mock.oneArg(true));
        
        assertEquals("1", mock.oneArg((byte) 1));
        assertEquals("2", mock.oneArg('a'));
        assertEquals("3", mock.oneArg((double) 1));
        assertEquals("4", mock.oneArg((float) 1));
        assertEquals("5", mock.oneArg((int) 1));
        assertEquals("6", mock.oneArg((long) 1));
        assertEquals("7", mock.oneArg((short) 1));
        
        assertEquals("8", mock.oneArg("abcde"));
        assertEquals(null, mock.oneArg("aaaaa"));
        
        assertEquals("9", mock.oneArg(Object.class));
        
    }

    @Test
    public void orOverloaded() {
        stub(mock.oneArg(or(eq(false), eq(true)))).andReturn("0");
        stub(mock.oneArg(or(eq((byte) 1), eq((byte) 2)))).andReturn("1");
        stub(mock.oneArg(or(eq((char) 1), eq((char) 2)))).andReturn("2");
        stub(mock.oneArg(or(eq((double) 1), eq((double) 2)))).andReturn("3");
        stub(mock.oneArg(or(eq((float) 1), eq((float) 2)))).andReturn("4");
        stub(mock.oneArg(or(eq((int) 1), eq((int) 2)))).andReturn("5");
        stub(mock.oneArg(or(eq((long) 1), eq((long) 2)))).andReturn("6");
        stub(mock.oneArg(or(eq((short) 1), eq((short) 2)))).andReturn("7");
        stub(mock.oneArg(or(eq("asd"), eq("jkl")))).andReturn("8");
        stub(mock.oneArg(or(eq(this.getClass()), eq(Object.class)))).andReturn("9");

        assertEquals("0", mock.oneArg(true));
        assertEquals("0", mock.oneArg(false));
        
        assertEquals("1", mock.oneArg((byte) 2));
        assertEquals("2", mock.oneArg((char) 1));
        assertEquals("3", mock.oneArg((double) 2));
        assertEquals("4", mock.oneArg((float) 1));
        assertEquals("5", mock.oneArg((int) 2));
        assertEquals("6", mock.oneArg((long) 1));
        assertEquals("7", mock.oneArg((short) 1));
        
        assertEquals("8", mock.oneArg("jkl"));
        assertEquals("8", mock.oneArg("asd"));
        assertEquals(null, mock.oneArg("asdjkl"));
        
        assertEquals("9", mock.oneArg(Object.class));
        assertEquals(null, mock.oneArg(String.class));
    }

    @Test
    public void notOverloaded() {
        stub(mock.oneArg(not(eq(false)))).andReturn("0");
        stub(mock.oneArg(not(eq((byte) 1)))).andReturn("1");
        stub(mock.oneArg(not(eq('a')))).andReturn("2");
        stub(mock.oneArg(not(eq((double) 1)))).andReturn("3");
        stub(mock.oneArg(not(eq((float) 1)))).andReturn("4");
        stub(mock.oneArg(not(eq((int) 1)))).andReturn("5");
        stub(mock.oneArg(not(eq((long) 1)))).andReturn("6");
        stub(mock.oneArg(not(eq((short) 1)))).andReturn("7");
        stub(mock.oneArg(not(contains("a")))).andReturn("8");
        stub(mock.oneArg(not(isA(Class.class)))).andReturn("9");

        assertEquals("0", mock.oneArg(true));
        assertEquals(null, mock.oneArg(false));
        
        assertEquals("1", mock.oneArg((byte) 2));
        assertEquals("2", mock.oneArg('b'));
        assertEquals("3", mock.oneArg((double) 2));
        assertEquals("4", mock.oneArg((float) 2));
        assertEquals("5", mock.oneArg((int) 2));
        assertEquals("6", mock.oneArg((long) 2));
        assertEquals("7", mock.oneArg((short) 2));
        assertEquals("8", mock.oneArg("bcde"));
        
        assertEquals("9", mock.oneArg(new Object()));
        assertEquals(null, mock.oneArg(Class.class));
    }

    @Test
    public void lessOrEqualOverloaded() {
        stub(mock.oneArg(leq((byte) 1))).andReturn("1");
        stub(mock.oneArg(leq((double) 1))).andReturn("3");
        stub(mock.oneArg(leq((float) 1))).andReturn("4");
        stub(mock.oneArg(leq((int) 1))).andReturn("5");
        stub(mock.oneArg(leq((long) 1))).andReturn("6");
        stub(mock.oneArg(leq((short) 1))).andReturn("7");
        stub(mock.oneArg(leq(new BigDecimal("1")))).andReturn("8");

        assertEquals("1", mock.oneArg((byte) 1));
        assertEquals(null, mock.oneArg((byte) 2));
        
        assertEquals("3", mock.oneArg((double) 1));
        assertEquals("7", mock.oneArg((short) 0));
        assertEquals("4", mock.oneArg((float) -5));
        assertEquals("5", mock.oneArg((int) -2));
        assertEquals("6", mock.oneArg((long) -3));
        
        assertEquals("8", mock.oneArg(new BigDecimal("0.5")));
        assertEquals(null, mock.oneArg(new BigDecimal("1.1")));
    }

    @Test
    public void lessThanOverloaded() {
        stub(mock.oneArg(lt((byte) 1))).andReturn("1");
        stub(mock.oneArg(lt((double) 1))).andReturn("3");
        stub(mock.oneArg(lt((float) 1))).andReturn("4");
        stub(mock.oneArg(lt((int) 1))).andReturn("5");
        stub(mock.oneArg(lt((long) 1))).andReturn("6");
        stub(mock.oneArg(lt((short) 1))).andReturn("7");
        stub(mock.oneArg(lt(new BigDecimal("1")))).andReturn("8");

        assertEquals("1", mock.oneArg((byte) 0));
        assertEquals(null, mock.oneArg((byte) 1));
        
        assertEquals("3", mock.oneArg((double) 0));
        assertEquals("7", mock.oneArg((short) 0));
        assertEquals("4", mock.oneArg((float) -4));
        assertEquals("5", mock.oneArg((int) -34));
        assertEquals("6", mock.oneArg((long) -6));
        
        assertEquals("8", mock.oneArg(new BigDecimal("0.5")));
        assertEquals(null, mock.oneArg(new BigDecimal("23")));
    }

    @Test
    public void greaterOrEqualMatcherOverloaded() {
        stub(mock.oneArg(geq((byte) 1))).andReturn("1");
        stub(mock.oneArg(geq((double) 1))).andReturn("3");
        stub(mock.oneArg(geq((float) 1))).andReturn("4");
        stub(mock.oneArg(geq((int) 1))).andReturn("5");
        stub(mock.oneArg(geq((long) 1))).andReturn("6");
        stub(mock.oneArg(geq((short) 1))).andReturn("7");
        stub(mock.oneArg(geq(new BigDecimal("1")))).andReturn("8");

        assertEquals("1", mock.oneArg((byte) 2));
        assertEquals(null, mock.oneArg((byte) 0));
        
        assertEquals("3", mock.oneArg((double) 1));
        assertEquals("7", mock.oneArg((short) 2));
        assertEquals("4", mock.oneArg((float) 3));
        assertEquals("5", mock.oneArg((int) 4));
        assertEquals("6", mock.oneArg((long) 5));
        
        assertEquals("8", mock.oneArg(new BigDecimal("1.00")));
        assertEquals(null, mock.oneArg(new BigDecimal("0.9")));
    }

    @Test
    public void greaterThanMatcherOverloaded() {
        stub(mock.oneArg(gt((byte) 1))).andReturn("1");
        stub(mock.oneArg(gt((double) 1))).andReturn("3");
        stub(mock.oneArg(gt((float) 1))).andReturn("4");
        stub(mock.oneArg(gt((int) 1))).andReturn("5");
        stub(mock.oneArg(gt((long) 1))).andReturn("6");
        stub(mock.oneArg(gt((short) 1))).andReturn("7");
        stub(mock.oneArg(gt(new BigDecimal("1")))).andReturn("8");

        assertEquals("1", mock.oneArg((byte) 2));
        assertEquals(null, mock.oneArg((byte) 1));
        
        assertEquals("3", mock.oneArg((double) 2));
        assertEquals("7", mock.oneArg((short) 2));
        assertEquals("4", mock.oneArg((float) 3));
        assertEquals("5", mock.oneArg((int) 2));
        assertEquals("6", mock.oneArg((long) 5));
        
        assertEquals("8", mock.oneArg(new BigDecimal("1.5")));
        assertEquals(null, mock.oneArg(new BigDecimal("0.9")));
    }

    @Test
    public void compareToMatcher() {
        stub(mock.oneArg(cmpEq(new BigDecimal("1.5")))).andReturn("0");

        assertEquals("0", mock.oneArg(new BigDecimal("1.50")));
        assertEquals(null, mock.oneArg(new BigDecimal("1.51")));
    }

    @Test
    public void anyMatcher() {
        stub(mock.oneArg(anyBoolean())).andReturn("0");
        stub(mock.oneArg(anyByte())).andReturn("1");
        stub(mock.oneArg(anyChar())).andReturn("2");
        stub(mock.oneArg(anyDouble())).andReturn("3");
        stub(mock.oneArg(anyFloat())).andReturn("4");
        stub(mock.oneArg(anyInt())).andReturn("5");
        stub(mock.oneArg(anyLong())).andReturn("6");
        stub(mock.oneArg(anyShort())).andReturn("7");
        stub(mock.oneArg((String) anyObject())).andReturn("8");
        stub(mock.oneArg(anyObject())).andReturn("9");

        assertEquals("0", mock.oneArg(true));
        assertEquals("0", mock.oneArg(false));
        
        assertEquals("1", mock.oneArg((byte) 1));
        assertEquals("2", mock.oneArg((char) 1));
        assertEquals("3", mock.oneArg((double) 1));
        assertEquals("4", mock.oneArg((float) 889));
        assertEquals("5", mock.oneArg((int) 1));
        assertEquals("6", mock.oneArg((long) 1));
        assertEquals("7", mock.oneArg((short) 1));
        assertEquals("8", mock.oneArg("Test"));
        
        assertEquals("9", mock.oneArg(new Object()));
        assertEquals("9", mock.oneArg(new HashMap()));
    }

    @Test
    public void shouldArrayEqualsDealWithNullArray() throws Exception {
        Object[] nullArray = null;
        stub(mock.oneArray(aryEq(nullArray))).andReturn("null");
        
        assertEquals("null", mock.oneArray(nullArray));
        
        mock = mock(IMethods.class);
        
        try {
            verify(mock).oneArray(nullArray);
        } catch (VerificationError e) {
            String expected = "\n" +
            		"Wanted but not invoked:" +
            		"\n" +
            		"IMethods.oneArray(null)";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void arrayEqualsMatcher() {
        stub(mock.oneArray(aryEq(new boolean[] { true, false, false }))).andReturn("0");
        stub(mock.oneArray(aryEq(new byte[] { 1 }))).andReturn("1");
        stub(mock.oneArray(aryEq(new char[] { 1 }))).andReturn("2");
        stub(mock.oneArray(aryEq(new double[] { 1 }))).andReturn("3");
        stub(mock.oneArray(aryEq(new float[] { 1 }))).andReturn("4");
        stub(mock.oneArray(aryEq(new int[] { 1 }))).andReturn("5");
        stub(mock.oneArray(aryEq(new long[] { 1 }))).andReturn("6");
        stub(mock.oneArray(aryEq(new short[] { 1 }))).andReturn("7");
        stub(mock.oneArray(aryEq(new String[] { "Test" }))).andReturn("8");
        stub(mock.oneArray(aryEq(new Object[] { "Test", new Integer(4) }))).andReturn("9");
        
        assertEquals("0", mock.oneArray(new boolean[] { true, false, false }));
        assertEquals("1", mock.oneArray(new byte[] { 1 }));
        assertEquals("2", mock.oneArray(new char[] { 1 }));
        assertEquals("3", mock.oneArray(new double[] { 1 }));
        assertEquals("4", mock.oneArray(new float[] { 1 }));
        assertEquals("5", mock.oneArray(new int[] { 1 }));
        assertEquals("6", mock.oneArray(new long[] { 1 }));
        assertEquals("7", mock.oneArray(new short[] { 1 }));
        assertEquals("8", mock.oneArray(new String[] { "Test" }));
        assertEquals("9", mock.oneArray(new Object[] { "Test", new Integer(4) }));
        
        assertEquals(null, mock.oneArray(new Object[] { "Test", new Integer(999) }));
        assertEquals(null, mock.oneArray(new Object[] { "Test", new Integer(4), "x" }));
        
        assertEquals(null, mock.oneArray(new boolean[] { true, false }));
        assertEquals(null, mock.oneArray(new boolean[] { true, true, false }));
    }
    
    @Test
    public void greaterOrEqualMatcher() {
        stub(mock.oneArg(geq(7))).andReturn(">= 7");
        stub(mock.oneArg(lt(7))).andReturn("< 7");

        assertEquals(">= 7", mock.oneArg(7));
        assertEquals(">= 7", mock.oneArg(8));
        assertEquals(">= 7", mock.oneArg(9));

        assertEquals("< 7", mock.oneArg(6));
        assertEquals("< 7", mock.oneArg(6));
    }

    @Test
    public void greaterThanMatcher() {
        stub(mock.oneArg(gt(7))).andReturn("> 7");
        stub(mock.oneArg(leq(7))).andReturn("<= 7");

        assertEquals("> 7", mock.oneArg(8));
        assertEquals("> 7", mock.oneArg(9));
        assertEquals("> 7", mock.oneArg(10));

        assertEquals("<= 7", mock.oneArg(7));
        assertEquals("<= 7", mock.oneArg(6));
    }

    @Test
    public void lessOrEqualMatcher() {
        stub(mock.oneArg(leq(7))).andReturn("<= 7");
        stub(mock.oneArg(gt(7))).andReturn("> 7");

        assertEquals("<= 7", mock.oneArg(7));
        assertEquals("<= 7", mock.oneArg(6));
        assertEquals("<= 7", mock.oneArg(5));

        assertEquals("> 7", mock.oneArg(8));
        assertEquals("> 7", mock.oneArg(9));
    }

    @Test
    public void lessThanMatcher() {
        stub(mock.oneArg(lt(7))).andReturn("< 7");
        stub(mock.oneArg(geq(7))).andReturn(">= 7");

        assertEquals("< 7", mock.oneArg(5));
        assertEquals("< 7", mock.oneArg(6));
        assertEquals("< 7", mock.oneArg(4));

        assertEquals(">= 7", mock.oneArg(7));
        assertEquals(">= 7", mock.oneArg(8));
    }

    @Test
    public void orMatcher() {
        stub(mock.oneArg(anyInt())).andReturn("other");
        stub(mock.oneArg(or(eq(7), eq(9)))).andReturn("7 or 9");

        assertEquals("other", mock.oneArg(10));
        assertEquals("7 or 9", mock.oneArg(7));
        assertEquals("7 or 9", mock.oneArg(9));
    }

    @Test
    public void nullMatcher() {
        stub(mock.threeArgumentMethod(eq(1), isNull(), eq(""))).andReturn("1");
        stub(mock.threeArgumentMethod(eq(1), not(isNull()), eq(""))).andReturn("2");

        assertEquals("1", mock.threeArgumentMethod(1, null, ""));
        assertEquals("2", mock.threeArgumentMethod(1, new Object(), ""));
    }

    @Test
    public void notNullMatcher() {
        stub(mock.threeArgumentMethod(eq(1), notNull(), eq(""))).andReturn("1");
        stub(mock.threeArgumentMethod(eq(1), not(notNull()), eq(""))).andReturn("2");

        assertEquals("1", mock.threeArgumentMethod(1, new Object(), ""));
        assertEquals("2", mock.threeArgumentMethod(1, null, ""));
    }

    @Test
    public void findMatcher() {
        stub(mock.oneArg(find("([a-z]+)\\d"))).andReturn("1");

        assertEquals("1", mock.oneArg("ab12"));
        assertEquals(null, mock.oneArg("12345"));
    }
    
    @Test
    public void matchesMatcher() {
        stub(mock.oneArg(matches("[a-z]+\\d\\d"))).andReturn("1");
        stub(mock.oneArg(matches("\\d\\d\\d"))).andReturn("2");

        assertEquals("1", mock.oneArg("a12"));
        assertEquals("2", mock.oneArg("131"));
        assertEquals(null, mock.oneArg("blah"));
    }

    @Test
    public void containsMatcher() {
        stub(mock.oneArg(contains("ell"))).andReturn("1");
        stub(mock.oneArg(contains("ld"))).andReturn("2");

        assertEquals("1", mock.oneArg("hello"));
        assertEquals("2", mock.oneArg("world"));
        assertEquals(null, mock.oneArg("xlx"));
    }

    @Test
    public void startsWithMatcher() {
        stub(mock.oneArg(startsWith("ab"))).andReturn("1");
        stub(mock.oneArg(startsWith("bc"))).andReturn("2");

        assertEquals("1", mock.oneArg("ab quake"));
        assertEquals("2", mock.oneArg("bc quake"));
        assertEquals(null, mock.oneArg("ba quake"));
    }

    @Test
    public void endsWithMatcher() {
        stub(mock.oneArg(endsWith("ab"))).andReturn("1");
        stub(mock.oneArg(endsWith("bc"))).andReturn("2");

        assertEquals("1", mock.oneArg("xab"));
        assertEquals("2", mock.oneArg("xbc"));
        assertEquals(null, mock.oneArg("ac"));
    }

    @Test
    public void deltaMatcher() {
        stub(mock.oneArg(eq(1.0D, 0.1D))).andReturn("1");
        stub(mock.oneArg(eq(2.0D, 0.1D))).andReturn("2");
        stub(mock.oneArg(eq(1.0F, 0.1F))).andReturn("3");
        stub(mock.oneArg(eq(2.0F, 0.1F))).andReturn("4");
        stub(mock.oneArg(eq(2.0F, 0.1F))).andReturn("4");

        assertEquals("1", mock.oneArg(1.0));
        assertEquals("1", mock.oneArg(0.91));
        assertEquals("1", mock.oneArg(1.09));
        assertEquals("2", mock.oneArg(2.0));

        assertEquals("3", mock.oneArg(1.0F));
        assertEquals("3", mock.oneArg(0.91F));
        assertEquals("3", mock.oneArg(1.09F));
        assertEquals("4", mock.oneArg(2.1F));
        
        assertEquals(null, mock.oneArg(2.2F));
    }

    @Test
    public void sameMatcher() {
        Object one = new String("1243");
        Object two = new String("1243");
        Object three = new String("1243");

        assertNotSame(one, two);
        assertEquals(one, two);
        assertEquals(two, three);

        stub(mock.oneArg(same(one))).andReturn("1");
        stub(mock.oneArg(same(two))).andReturn("2");

        assertEquals("1", mock.oneArg(one));
        assertEquals("2", mock.oneArg(two));
        assertEquals(null, mock.oneArg(three));
    }
}