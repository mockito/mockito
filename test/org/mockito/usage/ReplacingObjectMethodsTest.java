/*
 * Copyright (c) 2007 Szczepan Faber 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

public class ReplacingObjectMethodsTest {

    /**
     * Class overwrites object methods but implementation from Mockito will be called anyway.
     */
    public static class ClassToMockWithOverride {

        public boolean equals(Object o) {
            return false;
        }

        public int hashCode() {
            return -1;
        }

        public String toString() {
            return "super";
        }
    }
    
    public static class ClassWithAnotherOverride extends ClassToMockWithOverride {
        
        public String toString() {
            return "super.super";
        }
    }

    @Test 
    public void testShouldReplaceObjectMethods() {
        Object mock = Mockito.mock(ClassToMockWithOverride.class);
        assertThat(mock, equalTo(mock));
        assertThat(mock.hashCode(), not(equalTo(-1)));
        assertThat(mock.toString(), not(equalTo("super")));
    }
    
    @Test 
    public void testShouldReplaceObjectMethodsWhenOverridden() {
        Object mock = Mockito.mock(ClassToMockWithOverride.class);
        assertThat(mock, equalTo(mock));
        assertThat(mock.hashCode(), not(equalTo(-1)));
        assertThat(mock.toString(), not(equalTo("super")));
        assertThat(mock.toString(), not(equalTo("super.super")));
    }
    
    private interface DummyInterface {}
    private class DummyClass {}

    @Test
    public void shouldProvideMockyImplementationOfToString() {
        assertEquals("Mock for DummyClass", Mockito.mock(DummyClass.class).toString());
        assertEquals("Mock for DummyInterface", Mockito.mock(DummyInterface.class).toString());
    }
}