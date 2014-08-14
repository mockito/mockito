package org.mockito.internal.creation.bytebuddy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;

import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMaker implements MockMaker {

    private final ClassInstantiator classInstantiator;
    private final CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    public ByteBuddyMockMaker() {
        classInstantiator = initializeClassInstantiator();
        cachingMockBytecodeGenerator = new CachingMockBytecodeGenerator();
    }

    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
            throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
        }
        Class<? extends T> mockedType = cachingMockBytecodeGenerator.get(
                settings.getTypeToMock(),
                settings.getExtraInterfaces()
        );
        T mock = classInstantiator.instantiate(mockedType);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

        return ensureMockIsAssignableToMockedType(settings, mock);
    }

    private <T> T ensureMockIsAssignableToMockedType(MockCreationSettings<T> settings, T mock) {
        // force explicit cast to mocked type here, instead of
        // relying on the JVM to implicitly cast on the client call site
        Class<T> typeToMock = settings.getTypeToMock();
        try {
            return typeToMock.cast(mock);
        } catch (ClassCastException cce) {
            throw new MockitoException(join(
                    "ClassCastException occurred while creating the mockito mock :",
                    "  class to mock : '" + typeToMock.getCanonicalName() + "', loaded by classloader : '" + typeToMock.getClassLoader() + "'",
                    "  imposterizing class : '" + mock.getClass().getCanonicalName() + "', loaded by classloader : '" + mock.getClass().getClassLoader() + "'",
                    "",
                    "You might experience classloading issues, please ask the mockito mailing-list.",
                    ""
            ),cce);
        }
    }

    public MockHandler getHandler(Object mock) {
        if (!(mock instanceof MockMethodInterceptor.MockAccess)) {
            return null;
        }
        return ((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler();
    }

    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(
                new MockMethodInterceptor(asInternalMockHandler(newHandler), settings)
        );
    }

    private static ClassInstantiator initializeClassInstantiator() {
        try {
            Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
            Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
            return ClassInstantiator.class.cast(usingClassCacheConstructor.newInstance(new GlobalConfiguration().enableClassCache()));
        } catch (Throwable throwable) {
            // MockitoException cannot be used at this point as we are early in the classloading chain and necessary dependencies may not yet be loadable by the classloader
            throw new IllegalStateException(join(
                    "Mockito could not create mock: Objenesis is missing on the classpath.",
                    "Please add Objenesis on the classpath.",
                    ""
            ), throwable);
        }
    }

    private static InternalMockHandler asInternalMockHandler(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException(join(
                    "At the moment you cannot provide own implementations of MockHandler.",
                    "Please see the javadocs for the MockMaker interface.",
                    ""
            ));
        }
        return (InternalMockHandler) handler;
    }
}
