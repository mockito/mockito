/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.IMockHandler;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.creation.cglib.CGLIBHacker;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.FilteredCGLIBProxyRealMethod;
import org.mockito.internal.progress.SequenceNumber;

import java.io.Serializable;
import java.lang.reflect.Method;

public class MethodInterceptorFilter implements MethodInterceptor, Serializable {

    private static final long serialVersionUID = 6182795666612683784L;
    private final IMockHandler mockHandler;
    CGLIBHacker cglibHacker = new CGLIBHacker();
    ObjectMethodsGuru objectMethodsGuru = new ObjectMethodsGuru();

    public MethodInterceptorFilter(IMockHandler mockHandler) {
        this.mockHandler = mockHandler;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable {
        if (objectMethodsGuru.isEqualsMethod(method)) {
            return proxy == args[0];
        } else if (objectMethodsGuru.isHashCodeMethod(method)) {
            return hashCodeForMock(proxy);
        }

        cglibHacker.setMockitoNamingPolicy(methodProxy);
        
        FilteredCGLIBProxyRealMethod realMethod = new FilteredCGLIBProxyRealMethod(new MockitoMethodProxy(methodProxy));
        Invocation invocation = new Invocation(proxy, method, args, SequenceNumber.next(), realMethod);
        return mockHandler.handle(invocation);
    }
    
    public IMockHandler getMockHandler() {
        return mockHandler;
    }

    private int hashCodeForMock(Object mock) {
        return System.identityHashCode(mock);
    }
}