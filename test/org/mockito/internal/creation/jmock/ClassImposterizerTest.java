/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.jmock;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.creation.instance.ConstructorInstanceFactory;
import org.mockito.internal.creation.instance.ObjenesisInstanceFactory;
import org.mockitoutil.TestBase;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

@SuppressWarnings("unchecked")
public class ClassImposterizerTest extends TestBase {

    @Test
    public void shouldCreateMockFromInterface() throws Exception {
        SomeInterface proxy = ClassImposterizer.INSTANCE.imposterise(new ObjenesisInstanceFactory(), new MethodInterceptorStub(), SomeInterface.class);
        
        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(Object.class, superClass);
    }
    
    @Test
    public void shouldCreateMockFromClass() throws Exception {
        ClassWithoutConstructor proxy = ClassImposterizer.INSTANCE.imposterise(new ObjenesisInstanceFactory(), new MethodInterceptorStub(), ClassWithoutConstructor.class);
        
        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(ClassWithoutConstructor.class, superClass);
    }
    
    @Test
    public void shouldCreateMockFromClassEvenWhenConstructorIsDodgy() throws Exception {
        try {
            new ClassWithDodgyConstructor();
            fail();
        } catch (Exception e) {}
        
        ClassWithDodgyConstructor mock = ClassImposterizer.INSTANCE.imposterise(new ObjenesisInstanceFactory(), new MethodInterceptorStub(), ClassWithDodgyConstructor.class);
        assertNotNull(mock);
    }
    
    @Test 
    public void shouldMocksHaveDifferentInterceptors() throws Exception {
        SomeClass mockOne = ClassImposterizer.INSTANCE.imposterise(new ObjenesisInstanceFactory(), new MethodInterceptorStub(), SomeClass.class);
        SomeClass mockTwo = ClassImposterizer.INSTANCE.imposterise(new ObjenesisInstanceFactory(), new MethodInterceptorStub(), SomeClass.class);
        
        Factory cglibFactoryOne = (Factory) mockOne;
        Factory cglibFactoryTwo = (Factory) mockTwo;
        
        assertNotSame(cglibFactoryOne.getCallback(0), cglibFactoryTwo.getCallback(0));
    }
    
    @Test
    public void shouldUseAnicilliaryTypes() {
        SomeClass mock = ClassImposterizer.INSTANCE.imposterise(new ObjenesisInstanceFactory(), new MethodInterceptorStub(), SomeClass.class, SomeInterface.class);
        
        assertThat(mock, is(instanceOf(SomeInterface.class)));
    }

    @Test
    public void shouldCreateClassByConstructor() {
        OtherClass mock = ClassImposterizer.INSTANCE.imposterise(new ConstructorInstanceFactory(), new MethodInterceptorStub(), OtherClass.class);
        assertNotNull(mock);
    }

    class SomeClass {}
    interface SomeInterface {}
    static class OtherClass {}
    
    private class ClassWithoutConstructor {}

    private class ClassWithDodgyConstructor {
        public ClassWithDodgyConstructor() {
            throw new RuntimeException();
        }
    }

    private final class MethodInterceptorStub implements MethodInterceptor {

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return null;
        }
    }
}