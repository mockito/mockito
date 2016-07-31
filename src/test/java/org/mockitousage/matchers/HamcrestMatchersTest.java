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
import org.mockito.Mockito;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.*;

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
            assertThat(e).hasMessageContaining("contains 'X'");
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

    @Test
    public void supports_primitive_matchers_from_core_library() {
        mock.oneArg(true);
        mock.oneArg((byte) 1);
        mock.oneArg(2);
        mock.oneArg(3L);
        mock.oneArg('4');
        mock.oneArg(5.0D);
        mock.oneArg(6.0F);

        verify(mock).oneArg(booleanThat(is(true)));
        verify(mock).oneArg(byteThat(is((byte) 1)));
        verify(mock).oneArg(intThat(is(2)));
        verify(mock).oneArg(longThat(is(3L)));
        verify(mock).oneArg(charThat(is('4')));
        verify(mock).oneArg(doubleThat(is(5.0D)));
        verify(mock).oneArg(floatThat(is(6.0F)));
    }

    @SuppressWarnings("rawtypes")
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

    @SuppressWarnings("unchecked")
    private int nonGenericMatcher() {
        argThat(new NonGenericMatcher());
        return 0;
    }

    @Test
    public void coexists_with_mockito_matcher() {
        when(mock.simpleMethod(Mockito.argThat(new ArgumentMatcher<String>() {
            public boolean matches(String argument) {
                return true;
            }
        }))).thenReturn("x");

        assertEquals("x", mock.simpleMethod("x"));
    }
}