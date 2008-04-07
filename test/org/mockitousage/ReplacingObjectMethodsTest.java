/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.TestBase;

public class ReplacingObjectMethodsTest extends TestBase {

    private interface DummyInterface {}
    private class DummyClass {}
    
    @Test
    public void shouldProvideMockyImplementationOfToString() {
        DummyClass dummyClass = Mockito.mock(DummyClass.class);
        assertEquals("Mock for DummyClass, hashCode: " + dummyClass.hashCode(), dummyClass.toString());
        DummyInterface dummyInterface = Mockito.mock(DummyInterface.class);
        assertEquals("Mock for DummyInterface, hashCode: " + dummyInterface.hashCode(), dummyInterface.toString());
    }
    
    @Test 
    public void testShouldReplaceObjectMethods() {
        Object mock = Mockito.mock(ObjectMethodsOverridden.class);
        Object otherMock = Mockito.mock(ObjectMethodsOverridden.class);
        
        assertThat(mock, equalTo(mock));
        assertThat(mock, not(equalTo(otherMock)));
        
        assertThat(mock.hashCode(), not(equalTo(otherMock.hashCode())));
        
        assertThat(mock.toString(), contains("Mock for ObjectMethodsOverridden"));
    }
    
    @Test 
    public void testShouldReplaceObjectMethodsWhenOverridden() {
        Object mock = Mockito.mock(ObjectMethodsOverriddenSubclass.class);
        Object otherMock = Mockito.mock(ObjectMethodsOverriddenSubclass.class);
        
        assertThat(mock, equalTo(mock));
        assertThat(mock, not(equalTo(otherMock)));
        
        assertThat(mock.hashCode(), not(equalTo(otherMock.hashCode())));
        
        assertThat(mock.toString(), contains("Mock for ObjectMethodsOverriddenSubclass"));
    }
    
    public static class ObjectMethodsOverridden {
        public boolean equals(Object o) {
            throw new RuntimeException("Should not be called. MethodInterceptorFilter provides implementation");
        }
        public int hashCode() {
            throw new RuntimeException("Should not be called. MethodInterceptorFilter provides implementation");
        }
        public String toString() {
            throw new RuntimeException("Should not be called. MethodInterceptorFilter provides implementation");
        }
    }
    
    public static class ObjectMethodsOverriddenSubclass extends ObjectMethodsOverridden {
    }
}