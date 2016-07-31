/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.handler.MockHandlerFactory.createMockHandler;

import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.TypeMockability;

@SuppressWarnings("unchecked")
public class MockUtil {

    private static final MockMaker mockMaker = Plugins.getMockMaker();

    private MockUtil() {}

    public static TypeMockability typeMockabilityOf(Class<?> type) {
      return mockMaker.isTypeMockable(type);
    }

    public static <T> T createMock(MockCreationSettings<T> settings) {
        MockHandler mockHandler =  createMockHandler(settings);

        T mock = mockMaker.createMock(settings, mockHandler);

        Object spiedInstance = settings.getSpiedInstance();
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }

        return mock;
    }

    public static <T> void resetMock(T mock) {
        InternalMockHandler oldHandler = (InternalMockHandler) getMockHandler(mock);
        MockCreationSettings settings = oldHandler.getMockSettings();
        MockHandler newHandler = createMockHandler(settings);

        mockMaker.resetMock(mock, newHandler, settings);
    }

    public static <T> InternalMockHandler<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            MockHandler handler = mockMaker.getHandler(mock);
            return (InternalMockHandler) handler;
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    public static boolean isMock(Object mock) {
        // double check to avoid classes that have the same interfaces, could be great to have a custom mockito field in the proxy instead of relying on instance fields
        return isMockitoMock(mock);
    }

    public static boolean isSpy(Object mock) {
        return isMockitoMock(mock) && getMockSettings(mock).getDefaultAnswer() == Mockito.CALLS_REAL_METHODS;
    }

    private static <T> boolean isMockitoMock(T mock) {
        return mock != null && mockMaker.getHandler(mock) != null;
    }

    public static MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    public static void maybeRedefineMockName(Object mock, String newName) {
        MockName mockName = getMockName(mock);
        //TODO SF hacky...
        MockCreationSettings mockSettings = getMockHandler(mock).getMockSettings();
		if (mockName.isDefault() && mockSettings instanceof CreationSettings) {
            ((CreationSettings) mockSettings).setMockName(new MockNameImpl(newName));
        }
    }

    public static MockCreationSettings getMockSettings(Object mock) {
        return getMockHandler(mock).getMockSettings();
    }
}
