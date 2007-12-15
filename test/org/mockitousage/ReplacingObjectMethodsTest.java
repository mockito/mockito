/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;

public class ReplacingObjectMethodsTest extends RequiresValidState {

    private interface DummyInterface {}
    private class DummyClass {}
    
    @Test
    public void shouldProvideMockyImplementationOfToString() {
        assertEquals("Mock for DummyClass", Mockito.mock(DummyClass.class).toString());
        assertEquals("Mock for DummyInterface", Mockito.mock(DummyInterface.class).toString());
    }
    
    @Test 
    public void testShouldReplaceObjectMethods() {
        Object mock = Mockito.mock(ObjectMethodsOverridden.class);
        Object otherMock = Mockito.mock(ObjectMethodsOverridden.class);
        
        assertThat(mock, equalTo(mock));
        assertThat(mock, not(equalTo(otherMock)));
        
        assertThat(mock.hashCode(), not(equalTo(otherMock.hashCode())));
        
        assertThat(mock.toString(), equalTo("Mock for ObjectMethodsOverridden"));
    }
    
    @Test 
    public void testShouldReplaceObjectMethodsWhenOverridden() {
        Object mock = Mockito.mock(ObjectMethodsOverriddenSubclass.class);
        Object otherMock = Mockito.mock(ObjectMethodsOverriddenSubclass.class);
        
        assertThat(mock, equalTo(mock));
        assertThat(mock, not(equalTo(otherMock)));
        
        assertThat(mock.hashCode(), not(equalTo(otherMock.hashCode())));
        
        assertThat(mock.toString(), equalTo("Mock for ObjectMethodsOverriddenSubclass"));
    }
    
    public static class ObjectMethodsOverridden {
        public boolean equals(Object o) {
            throw new RuntimeException("Should not be called. ObjectMethodsFilter provides implementation");
        }
        public int hashCode() {
            throw new RuntimeException("Should not be called. ObjectMethodsFilter provides implementation");
        }
        public String toString() {
            throw new RuntimeException("Should not be called. ObjectMethodsFilter provides implementation");
        }
    }
    
    public static class ObjectMethodsOverriddenSubclass extends ObjectMethodsOverridden {
    }
}