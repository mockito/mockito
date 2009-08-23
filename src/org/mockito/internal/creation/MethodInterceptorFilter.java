/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.IMockHandler;
import org.mockito.internal.creation.cglib.CGLIBHacker;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.FilteredCGLIBProxyRealMethod;
import org.mockito.internal.progress.SequenceNumber;

import java.io.Serializable;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class MethodInterceptorFilter implements MethodInterceptor, Serializable {

    private final Method equalsMethod;
    private final Method hashCodeMethod;

    private final IMockHandler mockHandler;
    CGLIBHacker cglibHacker;

    public MethodInterceptorFilter(Class toMock, IMockHandler mockHandler) {
        try {
            if (toMock.isInterface()) {
                toMock = Object.class;
            }
            equalsMethod = toMock.getMethod("equals", Object.class);
            hashCodeMethod = toMock.getMethod("hashCode", (Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("\nSomething went really wrong. Object method could not be found!" +
                "\n please report it to the mocking mailing list at http://mockito.org");
        }
        this.mockHandler = mockHandler;
        this.cglibHacker = new CGLIBHacker();
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable {
        if (equalsMethod.equals(method)) {
            return proxy == args[0];
        } else if (hashCodeMethod.equals(method)) {
            return hashCodeForMock(proxy);
        }

        cglibHacker.setMockitoNamingPolicy(methodProxy);
        
        Invocation invocation = new Invocation(proxy, method, args, SequenceNumber.next(), new FilteredCGLIBProxyRealMethod(methodProxy));
        return mockHandler.handle(invocation);
    }
    
    public IMockHandler getMockHandler() {
        return mockHandler;
    }

    private int hashCodeForMock(Object mock) {
        return System.identityHashCode(mock);
    }
}