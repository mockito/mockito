/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.mockito.Mockito.argThat;

public class HamcrestMatchersTest extends TestBase {

    private final class ContainsX extends BaseMatcher<String> {
        public boolean matches(Object o) {
            return ((String) o).contains("X");
        }

        public void describeTo(Description d) {
            d.appendText("contains 'X'");
        }
    }

    @Mock
    private IMethods mock;

    @Test
    public void stubs_with_hamcrest_matcher() {
        when(mock.simpleMethod(argThat(new ContainsX()))).thenReturn("X");
        assertNull(mock.simpleMethod("blah"));
        assertEquals("X", mock.simpleMethod("blah X blah"));
    }
    
    @Test
    public void verifies_with_hamcrest_matcher() {
        mock.simpleMethod("blah");
        
        try {
            verify(mock).simpleMethod(argThat(new ContainsX()));
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertContains("contains 'X'", e.getMessage());
        }
    }

    private class IntMatcher extends BaseMatcher<Integer> {
        public boolean matches(Object o) {
            return true;
        }
        public void describeTo(Description description) {}
    }

    @Test
    public void supports_primitive_matchers() {
        when(mock.intArgumentReturningInt(argThat(new IntMatcher()))).thenReturn(5);
        assertEquals(5, mock.intArgumentReturningInt(10));
    }

    private class NonGenericMatcher extends BaseMatcher {
        public boolean matches(Object o) {
            return true;
        }
        public void describeTo(Description description) {}
    }

    @Test
    public void supports_non_generic_matchers() {
        when(mock.intArgumentReturningInt(nonGenericMatcher())).thenReturn(5);
        assertEquals(5, mock.intArgumentReturningInt(10));
    }

    private int nonGenericMatcher() {
        argThat(new NonGenericMatcher());
        return 0;
    }

    @Test
    public void coexists_with_mockito_matcher() {
        when(mock.simpleMethod(argThat(new ArgumentMatcher<String>() {
            public boolean matches(Object argument) {
                return true;
            }
        }))).thenReturn("x");

        assertEquals("x", mock.simpleMethod("x"));
    }
}