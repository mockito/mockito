/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.ClassPathLoader;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerFactory;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class MockUtil {

    private static final MockMaker mockMaker = ClassPathLoader.getMockMaker();

    public boolean isTypeMockable(Class<?> type) {
      return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers());
    }

    public <T> T createMock(MockCreationSettings<T> settings) {
        MockHandler mockHandler = new MockHandlerFactory().create(settings);

        T mock = mockMaker.createMock(settings, mockHandler);

        Object spiedInstance = settings.getSpiedInstance();
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }

        return mock;
    }

    public <T> void resetMock(T mock) {
        InternalMockHandler oldHandler = (InternalMockHandler) getMockHandler(mock);
        MockCreationSettings settings = oldHandler.getMockSettings();
        MockHandler newHandler = new MockHandlerFactory().create(settings);

        mockMaker.resetMock(mock, newHandler, settings);
    }

    public <T> InternalMockHandler<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return (InternalMockHandler) mockMaker.getHandler(mock);
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    public boolean isMock(Object mock) {
        // double check to avoid classes that have the same interfaces, could be great to have a custom mockito field in the proxy instead of relying on instance fields
        return mock instanceof MockitoMock && isMockitoMock(mock);
    }

    public boolean isSpy(Object mock) {
        return mock instanceof MockitoSpy;
    }

    public boolean isMock(Class mockClass) {
        return mockClass != null && MockitoMock.class.isAssignableFrom(mockClass);
    }

    public boolean isSpy(Class mockClass) {
        return mockClass != null && MockitoSpy.class.isAssignableFrom(mockClass);
    }

    private <T> boolean isMockitoMock(T mock) {
        return mockMaker.getHandler(mock) != null;
    }

    public MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    public void maybeRedefineMockName(Object mock, String newName) {
        MockName mockName = getMockName(mock);
        //TODO SF hacky...
        if (mockName.isDefault() && getMockHandler(mock).getMockSettings() instanceof CreationSettings) {
            ((CreationSettings) getMockHandler(mock).getMockSettings()).setMockName(new MockNameImpl(newName));
        }
    }

    public MockCreationSettings getMockSettings(Object mock) {
        return getMockHandler(mock).getMockSettings();
    }
}
