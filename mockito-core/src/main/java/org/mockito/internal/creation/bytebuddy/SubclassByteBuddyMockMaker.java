/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.internal.util.StringUtil.join;

import java.lang.reflect.Modifier;

import org.mockito.creation.instance.Instantiator;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.bytebuddy.access.MockAccess;
import org.mockito.internal.creation.bytebuddy.access.MockMethodInterceptor;
import org.mockito.internal.util.Platform;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

/**
 * Subclass based mock maker.
 *
 * This mock maker tries to create a subclass to represent a mock. It uses the given mock settings, that contains
 * the type to mock, extra interfaces, and serialization support.
 *
 * <p>
 * The type to mock has to be not final and not part of the JDK. The created mock will implement extra interfaces
 * if any. And will implement <code>Serializable</code> if this settings is explicitly set.
 */
public class SubclassByteBuddyMockMaker implements ClassCreatingMockMaker {

    private final BytecodeGenerator cachingMockBytecodeGenerator;
    private final BytecodeGenerator fallbackMockBytecodeGenerator;

    public SubclassByteBuddyMockMaker() {
        this(ModuleHandler.make());
    }

    public SubclassByteBuddyMockMaker(ModuleHandler handler) {
        cachingMockBytecodeGenerator =
                new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(handler), false);
        // Only used when the default strategy fails with a NoClassDefFoundError (e.g.
        // a superclass defined by another class loader in OSGi); kept separate so the
        // default path and its cache are completely unaffected.
        fallbackMockBytecodeGenerator =
                new TypeCachingBytecodeGenerator(
                        new SubclassBytecodeGenerator(handler, true), false);
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler<T> handler) {
        try {
            return doCreateMock(settings, handler, false, null);
        } catch (NoClassDefFoundError linkageError) {
            // In environments with several class loaders (e.g. OSGi), a superclass of
            // the mocked type can be defined by a class loader that the mock's class
            // loader cannot see. Since the generated mock redeclares inherited methods,
            // types referenced by the superclass must be resolvable by the mock's
            // loader, and defining or initializing the mock otherwise fails. Retry once
            // with the superclass loaders included. The default strategy above is
            // always tried first, so behavior for default usage is unchanged.
            return doCreateMock(settings, handler, true, linkageError);
        }
    }

    private <T> T doCreateMock(
            MockCreationSettings<T> settings,
            MockHandler<T> handler,
            boolean withSuperclassLoaders,
            NoClassDefFoundError linkageError) {
        Class<? extends T> mockedProxyType;
        if (withSuperclassLoaders) {
            if (!SubclassBytecodeGenerator.hasDistinctSuperclassLoaders(
                    settings.getTypeToMock())) {
                // The broader strategy would end up with the same class loader; retrying
                // cannot resolve the missing type.
                throw linkageError;
            }
            mockedProxyType = createMockTypeWithSuperclassLoaders(settings);
        } else {
            mockedProxyType = createMockType(settings);
        }
        Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(settings);
        T mockInstance = null;

        try {
            mockInstance = instantiator.newInstance(mockedProxyType);
            MockAccess mockAccess = (MockAccess) mockInstance;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(handler, settings));

            return ensureMockIsAssignableToMockedType(settings, mockInstance);
        } catch (ClassCastException cce) {
            throw new MockitoException(
                    join(
                            "ClassCastException occurred while creating the mockito mock :",
                            "  class to mock : " + describeClass(settings.getTypeToMock()),
                            "  created class : " + describeClass(mockedProxyType),
                            "  proxy instance class : " + describeClass(mockInstance),
                            "  instance creation by : " + instantiator.getClass().getSimpleName(),
                            "",
                            "You might experience classloading issues, please ask the mockito mailing-list.",
                            ""),
                    cce);
        } catch (org.mockito.creation.instance.InstantiationException e) {
            throw new MockitoException(
                    "Unable to create mock instance of type '"
                            + mockedProxyType.getSuperclass().getSimpleName()
                            + "'",
                    e);
        }
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        try {
            return cachingMockBytecodeGenerator.mockClass(
                    MockFeatures.withMockFeatures(
                            settings.getTypeToMock(),
                            settings.getExtraInterfaces(),
                            settings.getSerializableMode(),
                            settings.isStripAnnotations(),
                            settings.getDefaultAnswer()));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private <T> Class<? extends T> createMockTypeWithSuperclassLoaders(
            MockCreationSettings<T> settings) {
        try {
            return fallbackMockBytecodeGenerator.mockClass(
                    MockFeatures.withMockFeatures(
                            settings.getTypeToMock(),
                            settings.getExtraInterfaces(),
                            settings.getSerializableMode(),
                            settings.isStripAnnotations(),
                            settings.getDefaultAnswer()));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private static <T> T ensureMockIsAssignableToMockedType(
            MockCreationSettings<T> settings, T mock) {
        // Force explicit cast to mocked type here, instead of
        // relying on the JVM to implicitly cast on the client call site.
        // This allows us to catch earlier the ClassCastException earlier
        Class<T> typeToMock = settings.getTypeToMock();
        return typeToMock.cast(mock);
    }

    private <T> RuntimeException prettifyFailure(
            MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        if (mockFeatures.getTypeToMock().isArray()) {
            throw new MockitoException(
                    join("Mockito cannot mock arrays: " + mockFeatures.getTypeToMock() + ".", ""),
                    generationFailed);
        }
        if (Modifier.isPrivate(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(
                    join(
                            "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                            "Most likely it is due to mocking a private class that is not visible to Mockito",
                            ""),
                    generationFailed);
        }
        throw new MockitoException(
                join(
                        "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                        "",
                        "Mockito can only mock non-private & non-final classes, but the root cause of this error might be different.",
                        "Please check the full stacktrace to understand what the issue is.",
                        "If you're still not sure why you're getting this error, please open an issue on GitHub.",
                        "",
                        Platform.warnForVM(
                                "IBM J9 VM",
                                "Early IBM virtual machine are known to have issues with Mockito, please upgrade to an up-to-date version.\n",
                                "Hotspot",
                                ""),
                        Platform.describe(),
                        "",
                        "Underlying exception : " + generationFailed),
                generationFailed);
    }

    private static String describeClass(Class<?> type) {
        return type == null
                ? "null"
                : "'"
                        + type.getCanonicalName()
                        + "', loaded by classloader : '"
                        + type.getClassLoader()
                        + "'";
    }

    private static String describeClass(Object instance) {
        return instance == null ? "null" : describeClass(instance.getClass());
    }

    @Override
    public MockHandler<?> getHandler(Object mock) {
        if (!(mock instanceof MockAccess)) {
            return null;
        }
        return ((MockAccess) mock).getMockitoInterceptor().getMockHandler();
    }

    @Override
    public void resetMock(
            Object mock, MockHandler<?> newHandler, MockCreationSettings<?> settings) {
        ((MockAccess) mock).setMockitoInterceptor(new MockMethodInterceptor(newHandler, settings));
    }

    @Override
    public TypeMockability isTypeMockable(final Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return !type.isPrimitive()
                        && !Modifier.isFinal(type.getModifiers())
                        && !TypeSupport.INSTANCE.isSealed(type);
            }

            @Override
            public String nonMockableReason() {
                if (mockable()) {
                    return "";
                }
                if (type.isPrimitive()) {
                    return "primitive type";
                }
                if (Modifier.isFinal(type.getModifiers())) {
                    return "final class";
                }
                if (TypeSupport.INSTANCE.isSealed(type)) {
                    return "sealed class";
                }
                return join("not handled type");
            }
        };
    }

    @Override
    public void clearAllCaches() {
        cachingMockBytecodeGenerator.clearAllCaches();
    }
}
