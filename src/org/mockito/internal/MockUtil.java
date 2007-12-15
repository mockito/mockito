/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.ObjectMethodsFilter;
import org.mockito.internal.creation.MockFactory.MockMethodInterceptor;

public class MockUtil {
    
    private static MockMethodInterceptor getInterceptor(Object mock) {
        Factory factory = (Factory) mock;
        return (MockMethodInterceptor) factory.getCallback(0);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> MockControl<T> getControl(T mock) {
        if (mock == null) {
            throw new MockitoException("Mock cannot be null");
        }
        
        ObjectMethodsFilter<MockControl<T>> handler;

        try {
            if (Enhancer.isEnhanced(mock.getClass())) {
                handler = (ObjectMethodsFilter) getInterceptor(mock)
                        .getHandler();
            } else {
                throw new NotAMockException(mock);
            }
            
            return handler.getDelegate();
        } catch (ClassCastException e) {
            throw new NotAMockException(mock);
        }
    }
    
    public static void validateMock(Object mock) {
        getControl(mock);
    }
}