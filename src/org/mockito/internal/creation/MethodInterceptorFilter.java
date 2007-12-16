/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

@SuppressWarnings("unchecked")
public class MethodInterceptorFilter<T extends MockAwareInterceptor> implements MockAwareInterceptor {
    
    private final Method equalsMethod;
    private final Method hashCodeMethod;
    private final Method toStringMethod;

    private final T delegate;

    @SuppressWarnings("unchecked")
    public MethodInterceptorFilter(Class toMock, T delegate) {
        try {
            if (toMock.isInterface()) {
                toMock = Object.class;
            }
            equalsMethod = toMock.getMethod("equals", new Class[] { Object.class });
            hashCodeMethod = toMock.getMethod("hashCode", (Class[]) null);
            toStringMethod = toMock.getMethod("toString", (Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("An Object method could not be found!");
        }
        this.delegate = delegate;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable {
        if (method.isBridge()) {
            return methodProxy.invokeSuper(proxy, args);
        }
        
        if (equalsMethod.equals(method)) {
            return Boolean.valueOf(proxy == args[0]);
        } else if (hashCodeMethod.equals(method)) {
            return new Integer(System.identityHashCode(proxy));
        } else if (toStringMethod.equals(method)) {
            return mockToString(proxy);
        }
        
        return delegate.intercept(proxy, method, args, null);
    }

    private String mockToString(Object mock) {
        return "Mock for " + MockNamer.nameForMock(mock);
    }

    public T getDelegate() {
        return delegate;
    }

    public void setMock(Object mock) {
        delegate.setMock(mock);
    }
}