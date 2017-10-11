/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.TypeMockability;

import static org.mockito.internal.handler.MockHandlerFactory.createMockHandler;

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
        MockHandler oldHandler = getMockHandler(mock);
        MockCreationSettings settings = oldHandler.getMockSettings();
        MockHandler newHandler = createMockHandler(settings);

        mockMaker.resetMock(mock, newHandler, settings);
    }

    public static <T> MockHandler<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMock(mock)) {
            return mockMaker.getHandler(mock);
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    public static InvocationContainerImpl getInvocationContainer(Object mock) {
        return (InvocationContainerImpl) getMockHandler(mock).getInvocationContainer();
    }

    public static boolean isSpy(Object mock) {
        return isMock(mock) && getMockSettings(mock).getDefaultAnswer() == Mockito.CALLS_REAL_METHODS;
    }

    public static boolean isMock(Object mock) {
        // TODO SF (perf tweak) in our codebase we call mockMaker.getHandler() multiple times unnecessarily
        // This is not ideal because getHandler() can be expensive (reflective calls inside mock maker)
        // The frequent pattern in the codebase are separate calls to: 1) isMock(mock) then 2) getMockHandler(mock)
        // We could replace it with using mockingDetails().isMock()
        // Let's refactor the codebase and use new mockingDetails() in all relevant places.
        // Potentially we could also move other methods to MockitoMock, some other candidates: getInvocationContainer, isSpy, etc.
        // This also allows us to reuse our public API MockingDetails
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
