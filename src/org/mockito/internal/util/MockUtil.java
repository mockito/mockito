/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockFactory;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;

public class MockUtil {
    
    public static <T> T createMock(Class<T> classToMock, String mockName, MockingProgress progress) {
        MockFactory<T> proxyFactory = new MockFactory<T>();
        MockHandler<T> mockHandler = new MockHandler<T>(mockName, progress, new MatchersBinder());
        MethodInterceptorFilter<MockHandler<T>> filter = new MethodInterceptorFilter<MockHandler<T>>(classToMock, mockHandler);
        return proxyFactory.createMock(classToMock, filter);
    }
    
    public static <T> MockHandler<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }
        
        try {
            if (Enhancer.isEnhanced(mock.getClass())) {
                return ((MethodInterceptorFilter<MockHandler<T>>) getInterceptor(mock)).getDelegate();
            } else {
                throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
            }
        } catch (ClassCastException e) {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static <T> MethodInterceptorFilter<MockHandler<T>> getInterceptor(T mock) {
        Factory factory = (Factory) mock;
        return (MethodInterceptorFilter) factory.getCallback(0);
    }
    
    public static void validateMock(Object mock) {
        getMockHandler(mock);
    }
    
    public static boolean isMock(Object mock) {
        try {
            getMockHandler(mock);
            return true;
        } catch (MockitoException e) {
            return false;
        }
    }

    public static String getMockName(Object mock) {
        return getMockHandler(mock).getMockName();
    }
}