/*
 * Copyright (c) 2003-2006 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;

public class MockitoProvidesObjectMethodsTest {

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
}