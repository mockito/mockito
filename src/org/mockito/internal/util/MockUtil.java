/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.lang.reflect.Modifier;

import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerFactory;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.plugins.MockMaker;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MockUtil {

    private static final MockMaker MOCK_MAKER = Plugins.getMockMaker();

    public boolean isTypeMockable(final Class<?> type) {
      return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers());
    }

    public <T> T createMock(final MockCreationSettings<T> settings) {
        final MockHandler mockHandler = new MockHandlerFactory().create(settings);

        final T mock = MOCK_MAKER.createMock(settings, mockHandler);

        final Object spiedInstance = settings.getSpiedInstance();
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }

        return mock;
    }

    public <T> void resetMock(final T mock) {
        final InternalMockHandler oldHandler = getMockHandler(mock);
        final MockCreationSettings settings = oldHandler.getMockSettings();
        final MockHandler newHandler = new MockHandlerFactory().create(settings);

        MOCK_MAKER.resetMock(mock, newHandler, settings);
    }

    public <T> InternalMockHandler<T> getMockHandler(final T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            final MockHandler handler = MOCK_MAKER.getHandler(mock);
            return (InternalMockHandler) handler;
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    public boolean isMock(final Object mock) {
        // double check to avoid classes that have the same interfaces, could be great to have a custom mockito field in the proxy instead of relying on instance fields
        return isMockitoMock(mock);
    }

    public boolean isSpy(final Object mock) {
        return isMockitoMock(mock) && getMockSettings(mock).getDefaultAnswer() == Mockito.CALLS_REAL_METHODS;
    }

    private <T> boolean isMockitoMock(final T mock) {
        return MOCK_MAKER.getHandler(mock) != null;
    }

    public MockName getMockName(final Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    public void maybeRedefineMockName(final Object mock, final String newName) {
        final MockName mockName = getMockName(mock);
        //TODO SF hacky...
        if (mockName.isDefault() && getMockHandler(mock).getMockSettings() instanceof CreationSettings) {
            ((CreationSettings) getMockHandler(mock).getMockSettings()).setMockName(new MockNameImpl(newName));
        }
    }

    public MockCreationSettings getMockSettings(final Object mock) {
        return getMockHandler(mock).getMockSettings();
    }
}
