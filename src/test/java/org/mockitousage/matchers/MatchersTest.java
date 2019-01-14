/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.RandomAccess;
import java.util.regex.Pattern;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.AdditionalMatchers.cmpEq;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.AdditionalMatchers.find;
import static org.mockito.AdditionalMatchers.geq;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.AdditionalMatchers.leq;
import static org.mockito.AdditionalMatchers.lt;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyByte;
import static org.mockito.Mockito.anyChar;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.anyShort;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.endsWith;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNotNull;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.notNull;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.startsWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SuppressWarnings("unchecked")
public class MatchersTest extends TestBase {
    private IMethods mock = Mockito.mock(IMethods.class);

    @Test
    public void and_overloaded() {
        when(mock.oneArg(and(eq(false), eq(false)))).thenReturn("0");
        when(mock.oneArg(and(eq((byte) 1), eq((byte) 1)))).thenReturn("1");
        when(mock.oneArg(and(eq('a'), eq('a')))).thenReturn("2");
        when(mock.oneArg(and(eq(1D), eq(1D)))).thenReturn("3");
        when(mock.oneArg(and(eq(1F), eq(1F)))).thenReturn("4");
        when(mock.oneArg(and(eq(1), eq(1)))).thenReturn("5");
        when(mock.oneArg(and(eq(1L), eq(1L)))).thenReturn("6");
        when(mock.oneArg(and(eq((short) 1), eq((short) 1)))).thenReturn("7");
        when(mock.oneArg(and(contains("a"), contains("d")))).thenReturn("8");
        when(mock.oneArg(and(isA(Class.class), eq(Object.class)))).thenReturn("9");

        assertEquals("0", mock.oneArg(false));
        assertEquals(null, mock.oneArg(true));

        assertEquals("1", mock.oneArg((byte) 1));
        assertEquals("2", mock.oneArg('a'));
        assertEquals("3", mock.oneArg(1D));
        assertEquals("4", mock.oneArg(1F));
        assertEquals("5", mock.oneArg(1));
        assertEquals("6", mock.oneArg(1L));
        assertEquals("7", mock.oneArg((short) 1));

        assertEquals("8", mock.oneArg("abcde"));
        assertEquals(null, mock.oneArg("aaaaa"));

        assertEquals("9", mock.oneArg(Object.class));
    }

    @Test
    public void or_overloaded() {
        when(mock.oneArg(or(eq(false), eq(true)))).thenReturn("0");
        when(mock.oneArg(or(eq((byte) 1), eq((byte) 2)))).thenReturn("1");
        when(mock.oneArg(or(eq((char) 1), eq((char) 2)))).thenReturn("2");
        when(mock.oneArg(or(eq(1D), eq(2D)))).thenReturn("3");
        when(mock.oneArg(or(eq(1F), eq(2F)))).thenReturn("4");
        when(mock.oneArg(or(eq(1), eq(2)))).thenReturn("5");
        when(mock.oneArg(or(eq(1L), eq(2L)))).thenReturn("6");
        when(mock.oneArg(or(eq((short) 1), eq((short) 2)))).thenReturn("7");
        when(mock.oneArg(or(eq("asd"), eq("jkl")))).thenReturn("8");
        when(mock.oneArg(or(eq(this.getClass()), eq(Object.class)))).thenReturn("9");

        assertEquals("0", mock.oneArg(true));
        assertEquals("0", mock.oneArg(false));

        assertEquals("1", mock.oneArg((byte) 2));
        assertEquals("2", mock.oneArg((char) 1));
        assertEquals("3", mock.oneArg(2D));
        assertEquals("4", mock.oneArg(1F));
        assertEquals("5", mock.oneArg(2));
        assertEquals("6", mock.oneArg(1L));
        assertEquals("7", mock.oneArg((short) 1));

        assertEquals("8", mock.oneArg("jkl"));
        assertEquals("8", mock.oneArg("asd"));
        assertEquals(null, mock.oneArg("asdjkl"));

        assertEquals("9", mock.oneArg(Object.class));
        assertEquals(null, mock.oneArg(String.class));
    }

    @Test
    public void not_overloaded() {
        when(mock.oneArg(not(eq(false)))).thenReturn("0");
        when(mock.oneArg(not(eq((byte) 1)))).thenReturn("1");
        when(mock.oneArg(not(eq('a')))).thenReturn("2");
        when(mock.oneArg(not(eq(1D)))).thenReturn("3");
        when(mock.oneArg(not(eq(1F)))).thenReturn("4");
        when(mock.oneArg(not(eq(1)))).thenReturn("5");
        when(mock.oneArg(not(eq(1L)))).thenReturn("6");
        when(mock.oneArg(not(eq((short) 1)))).thenReturn("7");
        when(mock.oneArg(not(contains("a")))).thenReturn("8");
        when(mock.oneArg(not(isA(Class.class)))).thenReturn("9");

        assertEquals("0", mock.oneArg(true));
        assertEquals(null, mock.oneArg(false));

        assertEquals("1", mock.oneArg((byte) 2));
        assertEquals("2", mock.oneArg('b'));
        assertEquals("3", mock.oneArg(2D));
        assertEquals("4", mock.oneArg(2F));
        assertEquals("5", mock.oneArg(2));
        assertEquals("6", mock.oneArg(2L));
        assertEquals("7", mock.oneArg((short) 2));
        assertEquals("8", mock.oneArg("bcde"));

        assertEquals("9", mock.oneArg(new Object()));
        assertEquals(null, mock.oneArg(Class.class));
    }

    @Test
    public void less_or_equal_overloaded() {
        when(mock.oneArg(leq((byte) 1))).thenReturn("1");
        when(mock.oneArg(leq(1D))).thenReturn("3");
        when(mock.oneArg(leq(1F))).thenReturn("4");
        when(mock.oneArg(leq(1))).thenReturn("5");
        when(mock.oneArg(leq(1L))).thenReturn("6");
        when(mock.oneArg(leq((short) 1))).thenReturn("7");
        when(mock.oneArg(leq(new BigDecimal("1")))).thenReturn("8");

        assertEquals("1", mock.oneArg((byte) 1));
        assertEquals(null, mock.oneArg((byte) 2));

        assertEquals("3", mock.oneArg(1D));
        assertEquals("7", mock.oneArg((short) 0));
        assertEquals("4", mock.oneArg(-5F));
        assertEquals("5", mock.oneArg(-2));
        assertEquals("6", mock.oneArg(-3L));

        assertEquals("8", mock.oneArg(new BigDecimal("0.5")));
        assertEquals(null, mock.oneArg(new BigDecimal("1.1")));
    }

    @Test
    public void less_than_overloaded() {
        when(mock.oneArg(lt((byte) 1))).thenReturn("1");
        when(mock.oneArg(lt(1D))).thenReturn("3");
        when(mock.oneArg(lt(1F))).thenReturn("4");
        when(mock.oneArg(lt(1))).thenReturn("5");
        when(mock.oneArg(lt(1L))).thenReturn("6");
        when(mock.oneArg(lt((short) 1))).thenReturn("7");
        when(mock.oneArg(lt(new BigDecimal("1")))).thenReturn("8");

        assertEquals("1", mock.oneArg((byte) 0));
        assertEquals(null, mock.oneArg((byte) 1));

        assertEquals("3", mock.oneArg(0D));
        assertEquals("7", mock.oneArg((short) 0));
        assertEquals("4", mock.oneArg(-4F));
        assertEquals("5", mock.oneArg(-34));
        assertEquals("6", mock.oneArg(-6L));

        assertEquals("8", mock.oneArg(new BigDecimal("0.5")));
        assertEquals(null, mock.oneArg(new BigDecimal("23")));
    }

    @Test
    public void greater_or_equal_matcher_overloaded() {
        when(mock.oneArg(geq((byte) 1))).thenReturn("1");
        when(mock.oneArg(geq(1D))).thenReturn("3");
        when(mock.oneArg(geq(1F))).thenReturn("4");
        when(mock.oneArg(geq(1))).thenReturn("5");
        when(mock.oneArg(geq(1L))).thenReturn("6");
        when(mock.oneArg(geq((short) 1))).thenReturn("7");
        when(mock.oneArg(geq(new BigDecimal("1")))).thenReturn("8");

        assertEquals("1", mock.oneArg((byte) 2));
        assertEquals(null, mock.oneArg((byte) 0));

        assertEquals("3", mock.oneArg(1D));
        assertEquals("7", mock.oneArg((short) 2));
        assertEquals("4", mock.oneArg(3F));
        assertEquals("5", mock.oneArg(4));
        assertEquals("6", mock.oneArg(5L));

        assertEquals("8", mock.oneArg(new BigDecimal("1.00")));
        assertEquals(null, mock.oneArg(new BigDecimal("0.9")));
    }

    @Test
    public void greater_than_matcher_overloaded() {
        when(mock.oneArg(gt((byte) 1))).thenReturn("1");
        when(mock.oneArg(gt(1D))).thenReturn("3");
        when(mock.oneArg(gt(1F))).thenReturn("4");
        when(mock.oneArg(gt(1))).thenReturn("5");
        when(mock.oneArg(gt(1L))).thenReturn("6");
        when(mock.oneArg(gt((short) 1))).thenReturn("7");
        when(mock.oneArg(gt(new BigDecimal("1")))).thenReturn("8");

        assertEquals("1", mock.oneArg((byte) 2));
        assertEquals(null, mock.oneArg((byte) 1));

        assertEquals("3", mock.oneArg(2D));
        assertEquals("7", mock.oneArg((short) 2));
        assertEquals("4", mock.oneArg(3F));
        assertEquals("5", mock.oneArg(2));
        assertEquals("6", mock.oneArg(5L));

        assertEquals("8", mock.oneArg(new BigDecimal("1.5")));
        assertEquals(null, mock.oneArg(new BigDecimal("0.9")));
    }

    @Test
    public void compare_to_matcher() {
        when(mock.oneArg(cmpEq(new BigDecimal("1.5")))).thenReturn("0");

        assertEquals("0", mock.oneArg(new BigDecimal("1.50")));
        assertEquals(null, mock.oneArg(new BigDecimal("1.51")));
    }

    @Test
    public void any_String_matcher() {
        when(mock.oneArg(anyString())).thenReturn("matched");

        assertEquals("matched", mock.oneArg(""));
        assertEquals("matched", mock.oneArg("any string"));
        assertEquals(null, mock.oneArg((String) null));
    }

    @Test
    public void any_matcher() {
        when(mock.forObject(any())).thenReturn("matched");

        assertEquals("matched", mock.forObject(123));
        assertEquals("matched", mock.forObject("any string"));
        assertEquals("matched", mock.forObject("any string"));
        assertEquals("matched", mock.forObject(null));
    }

    @Test
    public void any_T_matcher() {
        when(mock.oneArg(anyBoolean())).thenReturn("0");
        when(mock.oneArg(anyByte())).thenReturn("1");
        when(mock.oneArg(anyChar())).thenReturn("2");
        when(mock.oneArg(anyDouble())).thenReturn("3");
        when(mock.oneArg(anyFloat())).thenReturn("4");
        when(mock.oneArg(anyInt())).thenReturn("5");
        when(mock.oneArg(anyLong())).thenReturn("6");
        when(mock.oneArg(anyShort())).thenReturn("7");
        when(mock.oneArg((String) anyObject())).thenReturn("8");
        when(mock.oneArg(Mockito.<Object>anyObject())).thenReturn("9");
        when(mock.oneArg(any(RandomAccess.class))).thenReturn("10");

        assertEquals("0", mock.oneArg(true));
        assertEquals("0", mock.oneArg(false));

        assertEquals("1", mock.oneArg((byte) 1));
        assertEquals("2", mock.oneArg((char) 1));
        assertEquals("3", mock.oneArg(1D));
        assertEquals("4", mock.oneArg(889F));
        assertEquals("5", mock.oneArg(1));
        assertEquals("6", mock.oneArg(1L));
        assertEquals("7", mock.oneArg((short) 1));
        assertEquals("8", mock.oneArg("Test"));

        assertEquals("9", mock.oneArg(new Object()));
        assertEquals("9", mock.oneArg(new HashMap()));

        assertEquals("10", mock.oneArg(new ArrayList()));
    }

    @Test
    public void should_array_equals_deal_with_null_array() throws Exception {
        Object[] nullArray = null;
        when(mock.oneArray(aryEq(nullArray))).thenReturn("null");

        assertEquals("null", mock.oneArray(nullArray));

        mock = mock(IMethods.class);

        try {
            verify(mock).oneArray(aryEq(nullArray));
            fail();
        } catch (WantedButNotInvoked e) {
            assertThat(e).hasMessageContaining("oneArray(null)");
        }
    }

    @Test
    public void should_use_smart_equals_for_arrays() throws Exception {
        //issue 143
        mock.arrayMethod(new String[]{"one"});
        verify(mock).arrayMethod(eq(new String[]{"one"}));
        verify(mock).arrayMethod(new String[]{"one"});
    }

    @Test
    public void should_use_smart_equals_for_primitive_arrays() throws Exception {
        //issue 143
        mock.objectArgMethod(new int[]{1, 2});
        verify(mock).objectArgMethod(eq(new int[]{1, 2}));
        verify(mock).objectArgMethod(new int[]{1, 2});
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void array_equals_should_throw_ArgumentsAreDifferentException_for_non_matching_arguments() {
        List<Object> list = Mockito.mock(List.class);

        list.add("test"); // testing fix for issue 20
        list.contains(new Object[]{"1"});

        Mockito.verify(list).contains(new Object[]{"1", "2", "3"});
    }

    @Test
    public void array_equals_matcher() {
        when(mock.oneArray(aryEq(new boolean[]{true, false, false}))).thenReturn("0");
        when(mock.oneArray(aryEq(new byte[]{1}))).thenReturn("1");
        when(mock.oneArray(aryEq(new char[]{1}))).thenReturn("2");
        when(mock.oneArray(aryEq(new double[]{1}))).thenReturn("3");
        when(mock.oneArray(aryEq(new float[]{1}))).thenReturn("4");
        when(mock.oneArray(aryEq(new int[]{1}))).thenReturn("5");
        when(mock.oneArray(aryEq(new long[]{1}))).thenReturn("6");
        when(mock.oneArray(aryEq(new short[]{1}))).thenReturn("7");
        when(mock.oneArray(aryEq(new String[]{"Test"}))).thenReturn("8");
        when(mock.oneArray(aryEq(new Object[]{"Test", new Integer(4)}))).thenReturn("9");

        assertEquals("0", mock.oneArray(new boolean[]{true, false, false}));
        assertEquals("1", mock.oneArray(new byte[]{1}));
        assertEquals("2", mock.oneArray(new char[]{1}));
        assertEquals("3", mock.oneArray(new double[]{1}));
        assertEquals("4", mock.oneArray(new float[]{1}));
        assertEquals("5", mock.oneArray(new int[]{1}));
        assertEquals("6", mock.oneArray(new long[]{1}));
        assertEquals("7", mock.oneArray(new short[]{1}));
        assertEquals("8", mock.oneArray(new String[]{"Test"}));
        assertEquals("9", mock.oneArray(new Object[]{"Test", new Integer(4)}));

        assertEquals(null, mock.oneArray(new Object[]{"Test", new Integer(999)}));
        assertEquals(null, mock.oneArray(new Object[]{"Test", new Integer(4), "x"}));

        assertEquals(null, mock.oneArray(new boolean[]{true, false}));
        assertEquals(null, mock.oneArray(new boolean[]{true, true, false}));
    }

    @Test
    public void greater_or_equal_matcher() {
        when(mock.oneArg(geq(7))).thenReturn(">= 7");
        when(mock.oneArg(lt(7))).thenReturn("< 7");

        assertEquals(">= 7", mock.oneArg(7));
        assertEquals(">= 7", mock.oneArg(8));
        assertEquals(">= 7", mock.oneArg(9));

        assertEquals("< 7", mock.oneArg(6));
        assertEquals("< 7", mock.oneArg(6));
    }

    @Test
    public void greater_than_matcher() {
        when(mock.oneArg(gt(7))).thenReturn("> 7");
        when(mock.oneArg(leq(7))).thenReturn("<= 7");

        assertEquals("> 7", mock.oneArg(8));
        assertEquals("> 7", mock.oneArg(9));
        assertEquals("> 7", mock.oneArg(10));

        assertEquals("<= 7", mock.oneArg(7));
        assertEquals("<= 7", mock.oneArg(6));
    }

    @Test
    public void less_or_equal_matcher() {
        when(mock.oneArg(leq(7))).thenReturn("<= 7");
        when(mock.oneArg(gt(7))).thenReturn("> 7");

        assertEquals("<= 7", mock.oneArg(7));
        assertEquals("<= 7", mock.oneArg(6));
        assertEquals("<= 7", mock.oneArg(5));

        assertEquals("> 7", mock.oneArg(8));
        assertEquals("> 7", mock.oneArg(9));
    }

    @Test
    public void less_than_matcher() {
        when(mock.oneArg(lt(7))).thenReturn("< 7");
        when(mock.oneArg(geq(7))).thenReturn(">= 7");

        assertEquals("< 7", mock.oneArg(5));
        assertEquals("< 7", mock.oneArg(6));
        assertEquals("< 7", mock.oneArg(4));

        assertEquals(">= 7", mock.oneArg(7));
        assertEquals(">= 7", mock.oneArg(8));
    }

    @Test
    public void or_matcher() {
        when(mock.oneArg(anyInt())).thenReturn("other");
        when(mock.oneArg(or(eq(7), eq(9)))).thenReturn("7 or 9");

        assertEquals("other", mock.oneArg(10));
        assertEquals("7 or 9", mock.oneArg(7));
        assertEquals("7 or 9", mock.oneArg(9));
    }

    @Test
    public void null_matcher() {
        when(mock.threeArgumentMethod(eq(1), isNull(), eq(""))).thenReturn("1");
        when(mock.threeArgumentMethod(eq(1), not(isNull()), eq(""))).thenReturn("2");

        assertEquals("1", mock.threeArgumentMethod(1, null, ""));
        assertEquals("2", mock.threeArgumentMethod(1, new Object(), ""));
    }

    @Test
    public void null_matcher_for_primitive_wrappers() {
        when(mock.forBoolean(isNull(Boolean.class))).thenReturn("ok");
        when(mock.forInteger(isNull(Integer.class))).thenReturn("ok");
        when(mock.forLong(isNull(Long.class))).thenReturn("ok");
        when(mock.forByte(isNull(Byte.class))).thenReturn("ok");
        when(mock.forShort(isNull(Short.class))).thenReturn("ok");
        when(mock.forCharacter(isNull(Character.class))).thenReturn("ok");
        when(mock.forDouble(isNull(Double.class))).thenReturn("ok");
        when(mock.forFloat(isNull(Float.class))).thenReturn("ok");

        assertEquals("ok", mock.forBoolean(null));
        assertEquals("ok", mock.forInteger(null));
        assertEquals("ok", mock.forLong(null));
        assertEquals("ok", mock.forByte(null));
        assertEquals("ok", mock.forShort(null));
        assertEquals("ok", mock.forCharacter(null));
        assertEquals("ok", mock.forDouble(null));
        assertEquals("ok", mock.forFloat(null));
    }

    @Test
    public void not_null_matcher() {
        when(mock.threeArgumentMethod(eq(1), notNull(), eq(""))).thenReturn("1");
        when(mock.threeArgumentMethod(eq(1), not(isNotNull()), eq(""))).thenReturn("2");

        assertEquals("1", mock.threeArgumentMethod(1, new Object(), ""));
        assertEquals("2", mock.threeArgumentMethod(1, null, ""));
    }

    @Test
    public void find_matcher() {
        when(mock.oneArg(find("([a-z]+)\\d"))).thenReturn("1");

        assertEquals("1", mock.oneArg("ab12"));
        assertEquals(null, mock.oneArg("12345"));
        assertEquals(null, mock.oneArg((Object) null));
    }

    @Test
    public void matches_matcher() {
        when(mock.oneArg(matches("[a-z]+\\d\\d"))).thenReturn("1");
        when(mock.oneArg(matches("\\d\\d\\d"))).thenReturn("2");

        assertEquals("1", mock.oneArg("a12"));
        assertEquals("2", mock.oneArg("131"));
        assertEquals(null, mock.oneArg("blah"));
    }

    @Test
    public void matches_Pattern_matcher() {
        when(mock.oneArg(matches(Pattern.compile("[a-z]+\\d\\d")))).thenReturn("1");
        when(mock.oneArg(matches(Pattern.compile("\\d\\d\\d")))).thenReturn("2");

        assertEquals("1", mock.oneArg("a12"));
        assertEquals("2", mock.oneArg("131"));
        assertEquals(null, mock.oneArg("blah"));
    }

    @Test
    public void contains_matcher() {
        when(mock.oneArg(contains("ell"))).thenReturn("1");
        when(mock.oneArg(contains("ld"))).thenReturn("2");

        assertEquals("1", mock.oneArg("hello"));
        assertEquals("2", mock.oneArg("world"));
        assertEquals(null, mock.oneArg("xlx"));
    }

    @Test
    public void starts_with_matcher() {
        when(mock.oneArg(startsWith("ab"))).thenReturn("1");
        when(mock.oneArg(startsWith("bc"))).thenReturn("2");

        assertEquals("1", mock.oneArg("ab quake"));
        assertEquals("2", mock.oneArg("bc quake"));
        assertEquals(null, mock.oneArg("ba quake"));
    }

    @Test
    public void ends_with_matcher() {
        when(mock.oneArg(endsWith("ab"))).thenReturn("1");
        when(mock.oneArg(endsWith("bc"))).thenReturn("2");

        assertEquals("1", mock.oneArg("xab"));
        assertEquals("2", mock.oneArg("xbc"));
        assertEquals(null, mock.oneArg("ac"));
    }

    @Test
    public void delta_matcher() {
        when(mock.oneArg(eq(1.0D, 0.1D))).thenReturn("1");
        when(mock.oneArg(eq(2.0D, 0.1D))).thenReturn("2");
        when(mock.oneArg(eq(1.0F, 0.1F))).thenReturn("3");
        when(mock.oneArg(eq(2.0F, 0.1F))).thenReturn("4");
        when(mock.oneArg(eq(2.0F, 0.1F))).thenReturn("4");

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
    public void delta_matcher_prints_itself() {
        try {
            verify(mock).oneArg(eq(1.0D, 0.1D));
            fail();
        } catch (WantedButNotInvoked e) {
            assertThat(e).hasMessageContaining("eq(1.0, 0.1)");
        }
    }

    @Test
    public void same_matcher() {
        Object one = new String("1243");
        Object two = new String("1243");
        Object three = new String("1243");

        assertNotSame(one, two);
        assertEquals(one, two);
        assertEquals(two, three);

        when(mock.oneArg(same(one))).thenReturn("1");
        when(mock.oneArg(same(two))).thenReturn("2");

        assertEquals("1", mock.oneArg(one));
        assertEquals("2", mock.oneArg(two));
        assertEquals(null, mock.oneArg(three));
    }

    @Test
    public void eq_matcher_and_nulls() {
        mock.simpleMethod((Object) null);

        verify(mock).simpleMethod(Mockito.<Object>eq(null));
    }

    @Test
    public void same_matcher_and_nulls() {
        mock.simpleMethod((Object) null);

        verify(mock).simpleMethod(Mockito.<Object>same(null));
    }

    @Test
    public void nullable_matcher() throws Exception {
        // imagine a Stream.of(...).map(c -> mock.oneArg(c))...
        mock.oneArg((Character) null);
        mock.oneArg(Character.valueOf('€'));

        verify(mock, times(2)).oneArg(nullable(Character.class));
    }
}
