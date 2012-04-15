/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.configuration.ClassPathLoader;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.plugins.MockMaker;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MockUtil {

    private static final MockMaker mockMaker = ClassPathLoader.getMockMaker();

    public <T> T createMock(MockCreationSettings<T> settings) {
        InvocationNotifierHandler<T> mockHandler = new InvocationNotifierHandler<T>(
                new MockHandler<T>(settings), settings);
        T mock = mockMaker.createMock(settings, mockHandler);

        Object spiedInstance = settings.getSpiedInstance();
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }

        return mock;
    }

    public <T> void resetMock(T mock) {
        InvocationNotifierHandler oldHandler = (InvocationNotifierHandler) getMockHandler(mock);
        MockCreationSettings settings = oldHandler.getMockSettings();
        InvocationNotifierHandler<T> newHandler = new InvocationNotifierHandler<T>(
                new MockHandler<T>(settings), settings);
        mockMaker.resetMock(mock, newHandler, settings);
    }

    public <T> MockHandlerInterface<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return (MockHandlerInterface) mockMaker.getHandler(mock);
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
}
