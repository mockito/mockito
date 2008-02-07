/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;
import net.sf.cglib.proxy.Factory;

import org.junit.Test;
import org.mockito.TestBase;

@SuppressWarnings("unchecked")
public class MockFactoryTest extends TestBase {

    @Test
    public void shouldCreateMockFromInterface() throws Exception {
        MockFactory<SomeInterface> factory = new MockFactory<SomeInterface>();
        SomeInterface proxy = factory.createMock(SomeInterface.class, new MockAwareStub());
        
        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(Object.class, superClass);
    }
    
    @Test
    public void shouldCreateMockFromClass() throws Exception {
        MockFactory<ClassWithoutConstructor> factory = new MockFactory<ClassWithoutConstructor>();
        ClassWithoutConstructor proxy = factory.createMock(ClassWithoutConstructor.class, new MockAwareStub());
        
        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(ClassWithoutConstructor.class, superClass);
    }
    
    @Test
    public void shouldCreateMockFromClassEvenWhenConstructorIsDodgy() throws Exception {
        try {
            new ClassWithDodgyConstructor();
            fail();
        } catch (Exception e) {}
        
        MockFactory<ClassWithDodgyConstructor> factory = new MockFactory<ClassWithDodgyConstructor>();
        ClassWithDodgyConstructor mock = factory.createMock(ClassWithDodgyConstructor.class, new MockAwareStub());
        assertNotNull(mock);
    }
    
    @Test 
    public void shouldMocksHaveDifferentInterceptors() throws Exception {
        MockFactory<SomeClass> factory = new MockFactory<SomeClass>();
        SomeClass mockOne = factory.createMock(SomeClass.class, new MockAwareStub());
        SomeClass mockTwo = factory.createMock(SomeClass.class, new MockAwareStub());
        
        Factory cglibFactoryOne = (Factory) mockOne;
        Factory cglibFactoryTwo = (Factory) mockTwo;
        
        assertNotSame(cglibFactoryOne.getCallback(0), cglibFactoryTwo.getCallback(0));
    }
    
    private interface SomeInterface {};

    private class SomeClass {};
    private class ClassWithoutConstructor {};
    
    private class ClassWithDodgyConstructor {
        public ClassWithDodgyConstructor() {
            throw new RuntimeException();
        }
    };
    
    private final class MockAwareStub extends MethodInterceptorFilter {
        public MockAwareStub() {
            super(Object.class, null);
        }

        public void setMock(Object mock) {}
    }
}