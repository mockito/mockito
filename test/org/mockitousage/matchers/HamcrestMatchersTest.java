/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.mockitoutil.TestBase;

public class HamcrestMatchersTest extends TestBase {

    //TODO SF integrate with hamcrest

    /*
    private final class ContainsX extends BaseMatcher<String> {
        public boolean matches(Object o) {
            return ((String) o).contains("X");
        }

        public void describeTo(Description d) {
            d.appendText("contains 'X'");
        }
    }

    @Mock private IMethods mock;

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
    */
}