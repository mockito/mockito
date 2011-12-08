/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class MockUtil {
    
    private final MockCreationValidator creationValidator;

    public MockUtil(MockCreationValidator creationValidator) {
        this.creationValidator = creationValidator;
    }
    
    public MockUtil() {
        this(new MockCreationValidator());
    }

    public <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());

        settings.initiateMockName(classToMock);

        MethodInterceptorFilter filter = newMethodInterceptorFilter(settings);
        Class<?>[] ancillaryTypes = prepareAncillaryTypes(settings);


        T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);

        Object spiedInstance = settings.getSpiedInstance();
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }
        
        return mock;
    }

    private Class<?>[] prepareAncillaryTypes(MockSettingsImpl settings) {
        Class<?>[] interfaces = settings.getExtraInterfaces();

        Class<?>[] ancillaryTypes;
        if (settings.isSerializable()) {
            ancillaryTypes = interfaces == null ? new Class<?>[] {Serializable.class} : new ArrayUtils().concat(interfaces, Serializable.class);
        } else {
            ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
        }
        if (settings.getSpiedInstance() != null) {
            ancillaryTypes = new ArrayUtils().concat(ancillaryTypes, MockitoSpy.class);
        }
        return ancillaryTypes;
    }

    public <T> void resetMock(T mock) {
        MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
        MethodInterceptorFilter newFilter = newMethodInterceptorFilter(oldMockHandler.getMockSettings());
        ((Factory) mock).setCallback(0, newFilter);
    }

    private <T> MethodInterceptorFilter newMethodInterceptorFilter(MockSettingsImpl settings) {
        MockHandler<T> mockHandler = new MockHandler<T>(settings);
        InvocationNotifierHandler<T> invocationNotifierHandler = new InvocationNotifierHandler<T>(mockHandler, settings);
        return new MethodInterceptorFilter(invocationNotifierHandler, settings);
    }

    public <T> MockHandlerInterface<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return (MockHandlerInterface) getInterceptor(mock).getHandler();
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    public boolean isMock(Object mock) {
        return mock != null && isMockitoMock(mock);
    }

    public boolean isSpy(Object mock) {
        return mock instanceof MockitoSpy && isMock(mock);
    }

    private <T> boolean isMockitoMock(T mock) {
        return getInterceptor(mock) != null;
    }

    private <T> MethodInterceptorFilter getInterceptor(T mock) {
        if (!(mock instanceof Factory)) {
            return null;
        }
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        if (callback instanceof MethodInterceptorFilter) {
            return (MethodInterceptorFilter) callback;
        }
        return null;
    }

    public MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    public void redefineMockNameIfSurrogate(Object mock, String newName) {
        if (getMockName(mock).isSurrogate()) {
            getMockHandler(mock).getMockSettings().redefineMockName(newName);
        }
    }
}
