/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.configuration.plugins.DefaultMockitoPlugins;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.TypeMockability;
import org.mockito.plugins.MockResolver;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.mockito.internal.handler.MockHandlerFactory.createMockHandler;

@SuppressWarnings("unchecked")
public class MockUtil {

    private static final MockMaker defaultMockMaker = Plugins.getMockMaker();
    private static final Map<Class<? extends MockMaker>, MockMaker> mockMakers =
            new ConcurrentHashMap<>(
                    Collections.singletonMap(defaultMockMaker.getClass(), defaultMockMaker));

    private MockUtil() {}

    private static MockMaker getMockMaker(String mockMaker) {
        if (mockMaker == null) {
            return defaultMockMaker;
        }

        String typeName;
        if (DefaultMockitoPlugins.MOCK_MAKER_ALIASES.contains(mockMaker)) {
            typeName = DefaultMockitoPlugins.getDefaultPluginClass(mockMaker);
        } else {
            typeName = mockMaker;
        }

        Class<? extends MockMaker> type;
        // Using the context class loader because PluginInitializer.loadImpl is using it as well.
        // Personally, I am suspicious whether the context class loader is a good choice in either
        // of these cases.
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        try {
            type = loader.loadClass(typeName).asSubclass(MockMaker.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load MockMaker: " + mockMaker, e);
        }

        return mockMakers.computeIfAbsent(
                type,
                t -> {
                    try {
                        return t.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new IllegalStateException(
                                "Failed to construct MockMaker: " + t.getName(), e);
                    }
                });
    }

    public static TypeMockability typeMockabilityOf(Class<?> type, String mockMaker) {
        return getMockMaker(mockMaker).isTypeMockable(type);
    }

    public static <T> T createMock(MockCreationSettings<T> settings) {
        MockMaker mockMaker = getMockMaker(settings.getMockMaker());
        MockHandler mockHandler = createMockHandler(settings);

        Object spiedInstance = settings.getSpiedInstance();

        T mock;
        if (spiedInstance != null) {
            mock =
                    mockMaker
                            .createSpy(settings, mockHandler, (T) spiedInstance)
                            .orElseGet(
                                    () -> {
                                        T instance = mockMaker.createMock(settings, mockHandler);
                                        new LenientCopyTool().copyToMock(spiedInstance, instance);
                                        return instance;
                                    });
        } else {
            mock = mockMaker.createMock(settings, mockHandler);
        }

        return mock;
    }

    public static void resetMock(Object mock) {
        MockHandler oldHandler = getMockHandler(mock);
        MockCreationSettings settings = oldHandler.getMockSettings();
        MockHandler newHandler = createMockHandler(settings);

        mock = resolve(mock);
        getMockMaker(settings.getMockMaker()).resetMock(mock, newHandler, settings);
    }

    public static MockHandler<?> getMockHandler(Object mock) {
        MockHandler handler = getMockHandlerOrNull(mock);
        if (handler != null) {
            return handler;
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    public static InvocationContainerImpl getInvocationContainer(Object mock) {
        return (InvocationContainerImpl) getMockHandler(mock).getInvocationContainer();
    }

    public static boolean isSpy(Object mock) {
        return isMock(mock)
                && getMockSettings(mock).getDefaultAnswer() == Mockito.CALLS_REAL_METHODS;
    }

    public static boolean isMock(Object mock) {
        // TODO SF (perf tweak) in our codebase we call mockMaker.getHandler() multiple times
        // unnecessarily
        // This is not ideal because getHandler() can be expensive (reflective calls inside mock
        // maker)
        // The frequent pattern in the codebase are separate calls to: 1) isMock(mock) then 2)
        // getMockHandler(mock)
        // We could replace it with using mockingDetails().isMock()
        // Let's refactor the codebase and use new mockingDetails() in all relevant places.
        // Potentially we could also move other methods to MockitoMock, some other candidates:
        // getInvocationContainer, isSpy, etc.
        // This also allows us to reuse our public API MockingDetails
        if (mock == null) {
            return false;
        }
        return getMockHandlerOrNull(mock) != null;
    }

    private static MockHandler<?> getMockHandlerOrNull(Object mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        mock = resolve(mock);

        for (MockMaker mockMaker : mockMakers.values()) {
            MockHandler<?> handler = mockMaker.getHandler(mock);
            if (handler != null) {
                assert getMockMaker(handler.getMockSettings().getMockMaker()) == mockMaker;
                return handler;
            }
        }
        return null;
    }

    private static Object resolve(Object mock) {
        if (mock instanceof Class<?>) { // static mocks are resolved by definition
            return mock;
        }
        for (MockResolver mockResolver : Plugins.getMockResolvers()) {
            mock = mockResolver.resolve(mock);
        }
        return mock;
    }

    public static boolean areSameMocks(Object mockA, Object mockB) {
        return mockA == mockB || resolve(mockA) == resolve(mockB);
    }

    public static MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    public static void maybeRedefineMockName(Object mock, String newName) {
        MockName mockName = getMockName(mock);
        // TODO SF hacky...
        MockCreationSettings mockSettings = getMockHandler(mock).getMockSettings();
        if (mockName.isDefault() && mockSettings instanceof CreationSettings) {
            ((CreationSettings) mockSettings).setMockName(new MockNameImpl(newName));
        }
    }

    public static MockCreationSettings getMockSettings(Object mock) {
        return getMockHandler(mock).getMockSettings();
    }

    public static <T> MockMaker.StaticMockControl<T> createStaticMock(
            Class<T> type, MockCreationSettings<T> settings) {
        MockMaker mockMaker = getMockMaker(settings.getMockMaker());
        MockHandler<T> handler = createMockHandler(settings);
        return mockMaker.createStaticMock(type, settings, handler);
    }

    public static <T> MockMaker.ConstructionMockControl<T> createConstructionMock(
            Class<T> type,
            Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory,
            MockedConstruction.MockInitializer<T> mockInitializer) {
        Function<MockedConstruction.Context, MockHandler<T>> handlerFactory =
                context -> createMockHandler(settingsFactory.apply(context));
        return defaultMockMaker.createConstructionMock(
                type, settingsFactory, handlerFactory, mockInitializer);
    }

    public static void clearAllCaches() {
        for (MockMaker mockMaker : mockMakers.values()) {
            mockMaker.clearAllCaches();
        }
    }
}
