/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.Method;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.creation.cglib.CGLIBHacker;

@SuppressWarnings("unchecked")
public class MethodInterceptorFilter<T extends MethodInterceptor> implements MethodInterceptor {
    
    private final Method equalsMethod;
    private final Method hashCodeMethod;

    private final T delegate;

    @SuppressWarnings("unchecked")
    public MethodInterceptorFilter(Class toMock, T delegate) {
        try {
            if (toMock.isInterface()) {
                toMock = Object.class;
            }
            equalsMethod = toMock.getMethod("equals", new Class[] { Object.class });
            hashCodeMethod = toMock.getMethod("hashCode", (Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("\nSomething went really wrong. Object method could not be found!" +
                "\n please report it to the mocking mailing list at http://mockito.org");
        }
        this.delegate = delegate;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable {
        if (equalsMethod.equals(method)) {
            return Boolean.valueOf(proxy == args[0]);
        } else if (hashCodeMethod.equals(method)) {
            return hashCodeForMock(proxy);
        }
        
        new CGLIBHacker().setMockitoNamingPolicy(methodProxy);
        
        return delegate.intercept(proxy, method, args, methodProxy);
    }

    private int hashCodeForMock(Object mock) {
        return new Integer(System.identityHashCode(mock));
    }

    public T getDelegate() {
        return delegate;
    }
}