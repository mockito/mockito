/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.jmock;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.junit.Test;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class ClassImposterizerTest extends TestBase {

    @Test
    public void shouldCreateMockFromInterface() throws Exception {
        SomeInterface proxy = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), SomeInterface.class);
        
        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(Object.class, superClass);
    }
    
    @Test
    public void shouldCreateMockFromClass() throws Exception {
        ClassWithoutConstructor proxy = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), ClassWithoutConstructor.class);
        
        Class superClass = proxy.getClass().getSuperclass();
        assertEquals(ClassWithoutConstructor.class, superClass);
    }
    
    @Test
    public void shouldCreateMockFromClassEvenWhenConstructorIsDodgy() throws Exception {
        try {
            new ClassWithDodgyConstructor();
            fail();
        } catch (Exception e) {}
        
        ClassWithDodgyConstructor mock = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), ClassWithDodgyConstructor.class);
        assertNotNull(mock);
    }
    
    @Test 
    public void shouldMocksHaveDifferentInterceptors() throws Exception {
        SomeClass mockOne = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), SomeClass.class);
        SomeClass mockTwo = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), SomeClass.class);
        
        Factory cglibFactoryOne = (Factory) mockOne;
        Factory cglibFactoryTwo = (Factory) mockTwo;
        
        assertNotSame(cglibFactoryOne.getCallback(0), cglibFactoryTwo.getCallback(0));
    }
    
    //TODO: try with 2 the same / different interfaces
    @Test
    public void ensureMockIsAllTypes() {
        SomeClass mock = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), SomeClass.class, SomeInterface.class);
        
        assertThat(mock, is(instanceOf(SomeInterface.class)));
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenClassPassedAsInterface() {
        ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), SomeInterface.class, SomeClass.class);
    }
    
    //TODO: different exception
    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenNullType() {
        ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), SomeClass.class, (Class<?>) null);
    }
    
    //TODO: why nulls are ok?
    @Test
    public void ensureNoProblemsWithNullTypes() {
        ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorStub(), SomeClass.class, (Class<?>[]) null);
    }
    
    private interface SomeInterface {};

    private class SomeClass {};
    private class ClassWithoutConstructor {};
    
    private class ClassWithDodgyConstructor {
        public ClassWithDodgyConstructor() {
            throw new RuntimeException();
        }
    };
    
    private final class MethodInterceptorStub implements MethodInterceptor {

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return null;
        }
    }
}