package org.mockito.internal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.sf.cglib.proxy.Factory;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class MockFactoryTest {

    @Test
    public void shouldCreateMockFromInterface() throws Exception {
        MockFactory<SomeInterface> factory = new MockFactory<SomeInterface>();
        SomeInterface proxy = factory.createMock(SomeInterface.class, new MockAwareStub());
        
        Class superClass = proxy.getClass().getSuperclass();
        assertThat(superClass, equalTo(Object.class));
    }
    
    @Test
    public void shouldCreateMockFromClass() throws Exception {
        MockFactory<ClassWithoutConstructor> factory = new MockFactory<ClassWithoutConstructor>();
        ClassWithoutConstructor proxy = factory.createMock(ClassWithoutConstructor.class, new MockAwareStub());
        
        Class superClass = proxy.getClass().getSuperclass();
        assertThat(superClass, equalTo(ClassWithoutConstructor.class));
    }
    
    @Test
    public void shouldCreateMockFromClassEvenWhenConstructorIsDodgy() throws Exception {
        MockFactory<ClassWithDodgyConstructor> factory = new MockFactory<ClassWithDodgyConstructor>();
        ClassWithDodgyConstructor mock = factory.createMock(ClassWithDodgyConstructor.class, new MockAwareStub());
        assertThat(mock, notNullValue());
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
    
    private final class MockAwareStub extends ObjectMethodsFilter {
        public MockAwareStub() {
            super(Object.class, null, null);
        }

        public void setMock(Object mock) {}
    }
}