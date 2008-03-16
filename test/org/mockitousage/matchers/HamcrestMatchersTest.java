/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.*;
import static org.mockito.util.ExtraMatchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class HamcrestMatchersTest extends TestBase {
    
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
        stub(mock.simpleMethod(argThat(new ContainsX()))).toReturn("X");
        assertNull(mock.simpleMethod("blah"));
        assertEquals("X", mock.simpleMethod("blah X blah"));
    }
    
    @Test
    public void shouldAcceptCollectionContainingMatcher() {
        stub(mock.simpleMethod(argThat(collectionHas("1", "2")))).toReturn("1 and 2");
        assertNull(mock.simpleMethod(Arrays.asList("3", "1")));
        assertEquals("1 and 2", mock.simpleMethod(Arrays.asList("1", "2")));
    }
    
    @Test
    public void shouldVerifyUsingHamcrestMatcher() {
        mock.simpleMethod("blah");
        
        try {
            verify(mock).simpleMethod(argThat(new ContainsX()));
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, messageContains("contains 'X'"));
        }
    }
}