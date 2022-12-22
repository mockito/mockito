/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.argumentmatching;

import static java.util.Collections.singletonList;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;
import org.mockito.internal.matchers.Equals;
import org.mockitoutil.TestBase;

@SuppressWarnings({"rawtypes", "unchecked", "serial"})
public class ArgumentMatchingToolTest extends TestBase {

    @Test
    public void shouldNotFindAnySuspiciousMatchersWhenNumberOfArgumentsDoesntMatch() {
        // given
        List<ArgumentMatcher> matchers = (List) Arrays.asList(new Equals(1));

        // when
        Integer[] suspicious =
                ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                        matchers, new Object[] {10, 20});

        // then
        assertEquals(0, suspicious.length);
    }

    @Test
    public void shouldNotFindAnySuspiciousMatchersWhenArgumentsMatch() {
        // given
        List<ArgumentMatcher> matchers = (List) Arrays.asList(new Equals(10), new Equals(20));

        // when
        Integer[] suspicious =
                ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                        matchers, new Object[] {10, 20});

        // then
        assertEquals(0, suspicious.length);
    }

    @Test
    public void shouldFindSuspiciousMatchers() {
        // given
        Equals matcherInt20 = new Equals(20);
        Long longPretendingAnInt = 20L;

        // when
        List<ArgumentMatcher> matchers = (List) Arrays.asList(new Equals(10), matcherInt20);
        Integer[] suspicious =
                ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                        matchers, new Object[] {10, longPretendingAnInt});

        // then
        assertEquals(1, suspicious.length);
        assertEquals(new Integer(1), suspicious[0]);
    }

    @Test
    public void shouldNotFindSuspiciousMatchersWhenTypesAreTheSame() {
        // given
        Equals matcherWithBadDescription =
                new Equals(20) {
                    public String toString() {
                        return "10";
                    }
                };
        Integer argument = 10;

        // when
        Integer[] suspicious =
                ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                        (List) Arrays.asList(matcherWithBadDescription), new Object[] {argument});

        // then
        assertEquals(0, suspicious.length);
    }

    @Test
    public void shouldWorkFineWhenGivenArgIsNull() {
        // when
        Integer[] suspicious =
                ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                        (List) Arrays.asList(new Equals(20)), new Object[] {null});

        // then
        assertEquals(0, suspicious.length);
    }

    @Test
    public void shouldUseMatchersSafely() {
        // This matcher is evil cause typeMatches(Object) returns true for every passed type but
        // matches(T)
        // method accepts only Strings. When a Integer is passed (thru the matches(Object) bridge
        // method )  a
        // ClassCastException will be thrown.
        class StringMatcher implements ArgumentMatcher<String>, ContainsExtraTypeInfo {
            @Override
            public boolean matches(String item) {
                return true; // in this test we never get here
            }

            @Override
            public String toStringWithType(String className) {
                return "";
            }

            @Override
            public boolean typeMatches(Object target) {
                return true;
            }

            @Override
            public Object getWanted() {
                return "";
            }
        }

        // given
        List<ArgumentMatcher> matchers = (List) singletonList(new StringMatcher());

        // when
        Integer[] suspicious =
                ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                        matchers, new Object[] {10});

        // then
        assertEquals(0, suspicious.length);
    }

    @Test
    public void shouldNotFindNonMatchingIndexesWhenEveryArgsMatch() {
        String arg1Value = "arg1";
        Integer arg2Value = 2222;

        Equals arg1 = new Equals(arg1Value);
        Equals arg2 = new Equals(arg2Value);

        List<Integer> indexes =
                ArgumentMatchingTool.getNotMatchingArgsIndexes(
                        Arrays.asList(arg1, arg2), new Object[] {arg1Value, arg2Value});

        assertEquals(Arrays.asList(), indexes);
    }

    @Test
    public void shouldFindNonMatchingIndexesWhenSingleArgDoesNotMatch() {
        String arg1Value = "arg1";
        Integer arg2Value = 2222;

        Equals arg1 = new Equals(arg1Value);
        Equals arg2 = new Equals(1111);

        List<Integer> indexes =
                ArgumentMatchingTool.getNotMatchingArgsIndexes(
                        Arrays.asList(arg1, arg2), new Object[] {arg1Value, arg2Value});

        assertEquals(Arrays.asList(1), indexes);
    }

    @Test
    public void shouldFindNonMatchingIndexesWhenMultiArgsDoNotMatch() {
        String arg1Value = "arg1";
        Integer arg2Value = 2222;

        Equals arg1 = new Equals("differs");
        Equals arg2 = new Equals(1111);

        List<Integer> indexes =
                ArgumentMatchingTool.getNotMatchingArgsIndexes(
                        Arrays.asList(arg1, arg2), new Object[] {arg1Value, arg2Value});

        assertEquals(Arrays.asList(0, 1), indexes);
    }
}
