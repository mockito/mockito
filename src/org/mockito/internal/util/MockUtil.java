/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.util.copy.LenientCopyTool;

public class MockUtil {
    
    public static CreationValidator creationValidator = new CreationValidator();

    public static <T> T createMock(Class<T> classToMock, MockingProgress progress, MockSettingsImpl settings) {
        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        
        MockName mockName = new MockName(settings.getMockName(), classToMock);
        MockHandler<T> mockHandler = new MockHandler<T>(mockName, progress, new MatchersBinder(), settings.getReturnValues());
        MethodInterceptorFilter<MockHandler<T>> filter = new MethodInterceptorFilter<MockHandler<T>>(classToMock, mockHandler);
        Class<?>[] ancillaryTypes = settings.getExtraInterfaces();
        Class<?>[] interfaces = ancillaryTypes == null ? new Class<?>[0] : ancillaryTypes;
        Object optionalInstance = settings.getSpiedInstance();
        
        T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, interfaces);
        
        if (optionalInstance != null) {
            new LenientCopyTool().copyToMock(optionalInstance, mock);
        }
        
        //TODO: does it make sense to set instance?
        filter.setInstance(optionalInstance != null ? optionalInstance : mock);
        return mock;
    }

    public static <T> void resetMock(T mock, MockingProgress progress) {
        MockHandler<T> oldMockHandler = (MockHandler<T>) getMockHandler(mock);
        MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler.getMockName(), progress, new MatchersBinder(), oldMockHandler.getReturnValues());
        MethodInterceptorFilter<MockHandler<T>> newFilter = new MethodInterceptorFilter<MockHandler<T>>(Object.class, newMockHandler);
        newFilter.setInstance(mock);
        ((Factory) mock).setCallback(0, newFilter);
    }

    public static <T> MockHandler<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return getInterceptor(mock).getDelegate();
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    private static <T> boolean isMockitoMock(T mock) {
        return Enhancer.isEnhanced(mock.getClass()) && getInterceptor(mock) != null;
    }

    public static boolean isMock(Object mock) {
        return mock != null && isMockitoMock(mock);
    }

    @SuppressWarnings("unchecked")
    private static <T> MethodInterceptorFilter<MockHandler<T>> getInterceptor(T mock) {
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        if (callback instanceof MethodInterceptorFilter) {
            return (MethodInterceptorFilter<MockHandler<T>>) callback;
        }
        return null;
    }

    public static MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockName();
    }
}