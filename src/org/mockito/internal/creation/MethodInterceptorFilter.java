/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.Method;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.cglib.CGLIBHacker;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.FilteredCGLIBProxyRealMethod;
import org.mockito.internal.progress.SequenceNumber;

@SuppressWarnings("unchecked")
public class MethodInterceptorFilter implements MethodInterceptor {
    
    private final Method equalsMethod;
    private final Method hashCodeMethod;

    private final MockHandler mockHandler;

    public MethodInterceptorFilter(Class toMock, MockHandler mockHandler) {
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
        this.mockHandler = mockHandler;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable {
        if (equalsMethod.equals(method)) {
            return Boolean.valueOf(proxy == args[0]);
        } else if (hashCodeMethod.equals(method)) {
            return hashCodeForMock(proxy);
        }
        
        new CGLIBHacker().setMockitoNamingPolicy(methodProxy);
        
        Invocation invocation = new Invocation(proxy, method, args, SequenceNumber.next(), new FilteredCGLIBProxyRealMethod(methodProxy));
        return mockHandler.handle(invocation);
    }
    
    public MockHandler getMockHandler() {
        return mockHandler;
    }

    private int hashCodeForMock(Object mock) {
        return new Integer(System.identityHashCode(mock));
    }
}