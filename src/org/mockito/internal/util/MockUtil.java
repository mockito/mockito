/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.util.copy.LenientCopyTool;

public class MockUtil {
    
    private final CreationValidator creationValidator;

    public MockUtil(CreationValidator creationValidator) {
        this.creationValidator = creationValidator;
    }
    
    public MockUtil() {
        this(new CreationValidator());
    }

    public <T> T createMock(Class<T> classToMock, MockingProgress progress, MockSettingsImpl settings) {
        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        
        MockName mockName = new MockName(settings.getMockName(), classToMock);
        MockHandler<T> mockHandler = new MockHandler<T>(mockName, progress, new MatchersBinder(), settings);
        MethodInterceptorFilter<MockHandler<T>> filter = new MethodInterceptorFilter<MockHandler<T>>(classToMock, mockHandler);
        Class<?>[] interfaces = settings.getExtraInterfaces();
        Class<?>[] ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
        Object spiedInstance = settings.getSpiedInstance();
        
        T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
        
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }
        
        return mock;
    }

    public <T> void resetMock(T mock, MockingProgress progress) {
        MockHandler<T> oldMockHandler = (MockHandler<T>) getMockHandler(mock);
        MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
        MethodInterceptorFilter<MockHandler<T>> newFilter = new MethodInterceptorFilter<MockHandler<T>>(Object.class, newMockHandler);
        ((Factory) mock).setCallback(0, newFilter);
    }

    public <T> MockHandler<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return getInterceptor(mock).getDelegate();
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    private <T> boolean isMockitoMock(T mock) {
        return Enhancer.isEnhanced(mock.getClass()) && getInterceptor(mock) != null;
    }

    public boolean isMock(Object mock) {
        return mock != null && isMockitoMock(mock);
    }

    @SuppressWarnings("unchecked")
    private <T> MethodInterceptorFilter<MockHandler<T>> getInterceptor(T mock) {
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        if (callback instanceof MethodInterceptorFilter) {
            return (MethodInterceptorFilter<MockHandler<T>>) callback;
        }
        return null;
    }

    public MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockName();
    }
}