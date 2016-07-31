/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;

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
    public void shouldReplaceObjectMethods() {
        Object mock = Mockito.mock(ObjectMethodsOverridden.class);
        Object otherMock = Mockito.mock(ObjectMethodsOverridden.class);

        assertThat(mock).isEqualTo(mock);
        assertThat(mock).isNotEqualTo(otherMock);
        assertThat(mock.hashCode()).isNotEqualTo(otherMock.hashCode());

        assertThat(mock.toString()).contains("Mock for ObjectMethodsOverridden");
    }
    
    @Test 
    public void shouldReplaceObjectMethodsWhenOverridden() {
        Object mock = Mockito.mock(ObjectMethodsOverriddenSubclass.class);
        Object otherMock = Mockito.mock(ObjectMethodsOverriddenSubclass.class);

        assertThat(mock).isEqualTo(mock);
        assertThat(mock).isNotEqualTo(otherMock);
        assertThat(mock.hashCode()).isNotEqualTo(otherMock.hashCode());

        assertThat(mock.toString()).contains("Mock for ObjectMethodsOverriddenSubclass");
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