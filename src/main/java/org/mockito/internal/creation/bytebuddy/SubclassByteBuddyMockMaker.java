/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.instance.Instantiator;
import org.mockito.internal.util.Platform;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Modifier;

import static org.mockito.internal.util.StringJoiner.join;

/**
 * Subclass based mock maker.
 *
 * This mock maker tries to create a subclass to represent a mock. It uses the given mock settings, that contains
 * the type to mock, extra interfaces, and serialization support.
 *
 * <p>
 * The type to mock has to be not final and not part of the JDK. THe created mock will implement extra interfaces
 * if any. And will implement <code>Serializable</code> if this settings is explicitly set.
 */
public class SubclassByteBuddyMockMaker implements ClassCreatingMockMaker {

    private final BytecodeGenerator cachingMockBytecodeGenerator;

    public SubclassByteBuddyMockMaker() {
        cachingMockBytecodeGenerator = new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(), false);
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        Class<? extends T> mockedProxyType = createMockType(settings);

        Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(settings);
        T mockInstance = null;
        try {
            mockInstance = instantiator.newInstance(mockedProxyType);
            MockAccess mockAccess = (MockAccess) mockInstance;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

            return ensureMockIsAssignableToMockedType(settings, mockInstance);
        } catch (ClassCastException cce) {
            throw new MockitoException(join(
                    "ClassCastException occurred while creating the mockito mock :",
                    "  class to mock : " + describeClass(settings.getTypeToMock()),
                    "  created class : " + describeClass(mockedProxyType),
                    "  proxy instance class : " + describeClass(mockInstance),
                    "  instance creation by : " + instantiator.getClass().getSimpleName(),
                    "",
                    "You might experience classloading issues, please ask the mockito mailing-list.",
                    ""
            ), cce);
        } catch (org.mockito.internal.creation.instance.InstantiationException e) {
            throw new MockitoException("Unable to create mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
        }
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        try {
            return cachingMockBytecodeGenerator.mockClass(MockFeatures.withMockFeatures(
                    settings.getTypeToMock(),
                    settings.getExtraInterfaces(),
                    settings.getSerializableMode()
            ));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private static <T> T ensureMockIsAssignableToMockedType(MockCreationSettings<T> settings, T mock) {
        // Force explicit cast to mocked type here, instead of
        // relying on the JVM to implicitly cast on the client call site.
        // This allows us to catch earlier the ClassCastException earlier
        Class<T> typeToMock = settings.getTypeToMock();
        return typeToMock.cast(mock);
    }

    private <T> RuntimeException prettifyFailure(MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        if (mockFeatures.getTypeToMock().isArray()) {
            throw new MockitoException(join(
                    "Mockito cannot mock arrays: " + mockFeatures.getTypeToMock() + ".",
                    ""
                    ), generationFailed);
        }
        if (Modifier.isPrivate(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(join(
                    "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                    "Most likely it is due to mocking a private class that is not visible to Mockito",
                    ""
            ), generationFailed);
        }
        throw new MockitoException(join(
                "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                "",
                "Mockito can only mock non-private & non-final classes.",
                "If you're not sure why you're getting this error, please report to the mailing list.",
                "",
                Platform.isJava8BelowUpdate45() ? "Java 8 early builds have bugs that were addressed in Java 1.8.0_45, please update your JDK!\n" : "",
                Platform.describe(),
                "",
                "Underlying exception : " + generationFailed
        ), generationFailed);
    }

    private static String describeClass(Class<?> type) {
        return type == null ? "null" : "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }

    private static String describeClass(Object instance) {
        return instance == null ? "null" : describeClass(instance.getClass());
    }

    @Override
    public MockHandler getHandler(Object mock) {
        if (!(mock instanceof MockAccess)) {
            return null;
        }
        return ((MockAccess) mock).getMockitoInterceptor().getMockHandler();
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MockAccess) mock).setMockitoInterceptor(
                new MockMethodInterceptor(asInternalMockHandler(newHandler), settings)
        );
    }

    @Override
    public TypeMockability isTypeMockable(final Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers());
            }

            @Override
            public String nonMockableReason() {
                if(mockable()) {
                    return "";
                }
                if (type.isPrimitive()) {
                    return "primitive type";
                }
                if (Modifier.isFinal(type.getModifiers())) {
                    return "final class";
                }
                return join("not handled type");
            }
        };
    }

    private static InternalMockHandler<?> asInternalMockHandler(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException(join(
                    "At the moment you cannot provide own implementations of MockHandler.",
                    "Please refer to the javadocs for the MockMaker interface.",
                    ""
            ));
        }
        return (InternalMockHandler<?>) handler;
    }
}
