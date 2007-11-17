package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import org.easymock.internal.matchers.Equals;
import org.junit.Test;


public class EqualsTest {
    
    @Test
    public void shouldBeEqual() {
        assertEquals(new Equals(null), new Equals(null));
        assertEquals(new Equals(new Integer(2)), new Equals(new Integer(2)));
        assertFalse(new Equals(null).equals(null));
        assertFalse(new Equals(null).equals("Test"));
        try {
            new Equals(null).hashCode();
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }
}
