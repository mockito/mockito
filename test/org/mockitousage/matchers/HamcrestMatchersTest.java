/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class HamcrestMatchersTest extends TestBase {

    private final class ContainsX extends BaseMatcher<String> {
        public boolean matches(Object o) {
            return ((String) o).contains("X");
        }

        public void describeTo(Description d) {
            d.appendText("contains 'X'");
        }
    }

    private final class PrimitiveMatcher extends BaseMatcher<Integer> {

        public boolean matches(Object o) {
            return true;
        }

        public void describeTo(Description description) {}
    }

    @Mock
    private IMethods mock;

    @Test
    public void shouldAcceptHamcrestMatcher() {
        when(mock.simpleMethod(argThat(new ContainsX()))).thenReturn("X");
        assertNull(mock.simpleMethod("blah"));
        assertEquals("X", mock.simpleMethod("blah X blah"));
    }
    
    @Test
    public void shouldVerifyUsingHamcrestMatcher() {
        mock.simpleMethod("blah");
        
        try {
            verify(mock).simpleMethod(argThat(new ContainsX()));
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertContains("contains 'X'", e.getMessage());
        }
    }

    //@Test TODO SF not implemented yet
    public void shouldMatchPrimitives() {
        when(mock.intArgumentReturningInt(argThat(new PrimitiveMatcher()))).thenReturn(5);
        assertEquals(5, mock.intArgumentReturningInt(10));
    }
}