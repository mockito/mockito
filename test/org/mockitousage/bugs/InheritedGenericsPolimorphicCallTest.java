/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
//see issue 200
public class InheritedGenericsPolimorphicCallTest extends TestBase {

    protected interface MyIterable<T> extends Iterable<T> {
        public MyIterator<T> iterator();
    }

    protected interface MyIterator<T> extends Iterator<T> {
        // adds nothing here
    }

    MyIterator<String> myIterator = Mockito.mock(MyIterator.class);
    MyIterable<String> iterable = Mockito.mock(MyIterable.class);

    @Test
    public void shouldStubbingWork() {
        Mockito.when(iterable.iterator()).thenReturn(myIterator);
        Assert.assertNotNull(((Iterable) iterable).iterator());
        Assert.assertNotNull(iterable.iterator());
    }
    
    @Test
    public void shouldVerificationWorks() {
        iterable.iterator();
        
        verify(iterable).iterator();
        verify((Iterable) iterable).iterator();
    }
    
    @Test
    public void shouldWorkExactlyAsJavaProxyWould() {
        //given
        final List<Method> methods = new LinkedList<Method>();
        InvocationHandler handler = new InvocationHandler() {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            methods.add(method);
            return null;
        }};
            
        iterable = (MyIterable) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[] { MyIterable.class },
                handler);

        //when
        iterable.iterator();
        ((Iterable) iterable).iterator();
        
        //then
        assertEquals(2, methods.size());
        assertEquals(methods.get(0), methods.get(1));
    }  
}