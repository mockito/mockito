/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.internal.matchers.MatcherMarkers;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class VerificationAndStubbingUsingMatchersTest extends TestBase {
    private IMethods one;
    private IMethods two;
    private IMethods three;
    private IMethods four;

    @Before
    public void setUp() {
        one = mock(IMethods.class);
        two = mock(IMethods.class);
        three = mock(IMethods.class);
        four = mock(IMethods.class);
    }

    @Test
    public void shouldStubUsingMatchers() {
        when(one.simpleMethod(2)).thenReturn("2");
        when(two.simpleMethod(anyString())).thenReturn("any");
        when(three.simpleMethod(startsWith("test"))).thenThrow(new RuntimeException());

        assertEquals(null, one.simpleMethod(1));
        assertEquals("2", one.simpleMethod(2));

        assertEquals("any", two.simpleMethod("two"));
        assertEquals("any", two.simpleMethod("two again"));

        assertEquals(null, three.simpleMethod("three"));
        assertEquals(null, three.simpleMethod("three again"));

        try {
            three.simpleMethod("test three again");
            fail();
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void shouldStubUsingMatchersMixedWithConcreteValues() {

        when(one.simpleMethod("1", eq(2))).thenReturn("2");
        when(two.simpleMethod(anyString(), 2)).thenReturn("any");
        when(three.simpleMethod(startsWith("test"), 2)).thenThrow(new RuntimeException());
        when(four.threeArgumentMethod(1, any(IMethods.class), "3")).thenReturn("x");

        assertEquals(null, one.simpleMethod("1", 10));
        assertEquals("2", one.simpleMethod("1", 2));

        assertEquals("any", two.simpleMethod("two", 2));
        assertEquals("any", two.simpleMethod("two again", 2));

        assertEquals(null, three.simpleMethod("three", 2));
        assertEquals(null, three.simpleMethod("three again", 2));

        try {
            three.simpleMethod("test three again", 2);
            fail();
        } catch (RuntimeException e) {
        }

        assertEquals("x", four.threeArgumentMethod(1, four, "3"));
    }

    @Test
    public void testStubUsingConcreteValuesEqualToMatchersMarkersAndNoOtherMatchers() {

        when(one.oneArg(MatcherMarkers.markerOf(int.class))).thenReturn("1");
        when(two.simpleMethod(MatcherMarkers.markerOf(String.class),
            MatcherMarkers.markerOf(Integer.class))).thenReturn("2");
        when(three.listArgMethod(MatcherMarkers.markerOf(List.class))).thenReturn("3");
        when(four.oneArg(MatcherMarkers.markerOf(IMethods.class))).thenReturn("4");

        assertEquals(null, one.oneArg(MatcherMarkers.markerOf(int.class) - 1));
        assertEquals("1", one.oneArg(MatcherMarkers.markerOf(int.class)));

        assertEquals(null, two.simpleMethod(MatcherMarkers.markerOf(String.class),
            MatcherMarkers.markerOf(Integer.class) - 1));
        assertEquals("2", two.simpleMethod(MatcherMarkers.markerOf(String.class),
            MatcherMarkers.markerOf(Integer.class)));

        assertEquals(null, three.listArgMethod(null));
        assertEquals("3", three.listArgMethod(MatcherMarkers.markerOf(List.class)));

        assertEquals(null, four.oneArg(four));
        assertEquals("4", four.oneArg(MatcherMarkers.markerOf(IMethods.class)));
    }

    @Test
    public void shouldVerifyUsingMatchers() {
        doThrow(new RuntimeException()).when(one).oneArg(true);
        when(three.varargsObject(5, "first arg", "second arg")).thenReturn("stubbed");

        try {
            one.oneArg(true);
            fail();
        } catch (RuntimeException e) {
        }

        one.simpleMethod(100);
        two.simpleMethod("test Mockito");
        three.varargsObject(10, "first arg", "second arg");

        assertEquals("stubbed", three.varargsObject(5, "first arg", "second arg"));

        verify(one).oneArg(eq(true));
        verify(one).simpleMethod(anyInt());
        verify(two).simpleMethod(startsWith("test"));
        verify(three).varargsObject(5, "first arg", "second arg");
        verify(three).varargsObject(eq(10), eq("first arg"), startsWith("second"));

        verifyNoMoreInteractions(one, two, three);

        try {
            verify(three).varargsObject(eq(10), eq("first arg"), startsWith("third"));
            fail();
        } catch (WantedButNotInvoked e) {
        }
    }

    @Test
    public void shouldVerifyUsingMatchersMixedWithConcreteValues() {

        one.twoArgumentMethod(1, 2);
        verify(one).twoArgumentMethod(anyInt(), 2);

        // matcher not matching actual invocation
        try {
            verify(one).twoArgumentMethod(eq(9), 2);
            fail();
        } catch (WantedButNotInvoked e) {
        }

        // concrete value not matching actual invocation
        try {
            verify(one).twoArgumentMethod(anyInt(), 55);
            fail();
        } catch (WantedButNotInvoked e) {
        }

        // both matcher and concrete value not matching actual invocation
        try {
            verify(one).twoArgumentMethod(eq(9), 55);
            fail();
        } catch (WantedButNotInvoked e) {
        }
    }

    @Test
    public void shouldVerifyUsingConcreteValuesEqualToMatchersMarkersAndNoOtherMatchers() {

        one.oneArg(MatcherMarkers.markerOf(int.class));
        verify(one).oneArg(MatcherMarkers.markerOf(int.class));
        verify(one).oneArg(eq(MatcherMarkers.markerOf(int.class)));
        verify(one).oneArg(any(Integer.class));

        two.simpleMethod(MatcherMarkers.markerOf(String.class), MatcherMarkers.markerOf(Integer.class));
        verify(two).simpleMethod(MatcherMarkers.markerOf(String.class), MatcherMarkers.markerOf(Integer.class));
        verify(two).simpleMethod(anyString(), anyInt());
        try {
            verify(two).simpleMethod(MatcherMarkers.markerOf(String.class), MatcherMarkers.markerOf(Integer.class) - 1);
            fail();
        } catch (WantedButNotInvoked e) {
        }


        three.listArgMethod(MatcherMarkers.markerOf(List.class));
        verify(three).listArgMethod(MatcherMarkers.markerOf(List.class));
        verify(three).listArgMethod(ArgumentMatchers.<String>anyList());
        try {
            verify(three).listArgMethod(null);
            fail();
        } catch (WantedButNotInvoked e) {
        }


        four.oneArg(MatcherMarkers.markerOf(IMethods.class));
        verify(four).oneArg(MatcherMarkers.markerOf(IMethods.class));
        verify(four).oneArg((Object) null);

        try {
            // since the marker is null, "any" shouldn't apply for it
            verify(four).oneArg(any(IMethods.class));
            fail();
        } catch (WantedButNotInvoked e) {
        }

        try {
            verify(four).oneArg(four);
            fail();
        } catch (WantedButNotInvoked e) {
        }
    }
}
