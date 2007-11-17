/*
 * Copyright (c) 2003-2006 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import org.mockito.internal.*;
import org.mockito.internal.ClassProxyFactory.MockMethodInterceptor;

public final class ClassExtensionHelper {

    private ClassExtensionHelper() {
    }
    
    public static MockMethodInterceptor getInterceptor(Object mock) {
        Factory factory = (Factory) mock;
        return (MockMethodInterceptor) factory.getCallback(0);
    }

    public static MocksControl getControl(Object mock) {
        MockitoObjectMethodsFilter<MockInvocationHandler> handler;

        try {
            if (Enhancer.isEnhanced(mock.getClass())) {
                handler = (MockitoObjectMethodsFilter) getInterceptor(mock)
                        .getHandler();
            } else if (Proxy.isProxyClass(mock.getClass())) {
                handler = (MockitoObjectMethodsFilter) Proxy
                        .getInvocationHandler(mock);
            } else {
                throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                        "Not a mock: " + mock.getClass().getName()));
            }
            
            return handler.getDelegate().getControl();
        } catch (ClassCastException e) {
            throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                    "Not a mock: " + mock.getClass().getName()));
        }
    }
}
