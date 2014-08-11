package org.mockito.internal.creation.bytebuddy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMaker implements MockMaker {

    private final ClassInstantiator classInstantiator;

    private static final Map<MockKey<?>, Class<?>> PREVIOUSLY_GENERATED_MOCK_CLASSES = new ConcurrentHashMap<MockKey<?>, Class<?>>();


    public ByteBuddyMockMaker() {
        classInstantiator = initializeClassInstantiator();
    }

    private static ClassInstantiator initializeClassInstantiator() {
        try {
            Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker$ClassInstantiator$UsingObjenesis");
            Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
            return ClassInstantiator.class.cast(usingClassCacheConstructor.newInstance(new GlobalConfiguration().enableClassCache()));
        } catch (Throwable throwable) {
            // MockitoException cannot be used at this point as we are early in the classloading chain and necessary dependencies may not yet be loadable by the classloader
            throw new IllegalStateException(join(
                    "Mockito could not create mock: Objenesis is missing on the classpath.",
                    "Please add Objenesis on the classpath.",
                    ""
            ));
        }
    }

    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
            throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddy");
        }
        Class<? extends T> mockedType = getOrMakeMock(
                settings.getTypeToMock(),
                settings.getExtraInterfaces()
        );
        T mock = classInstantiator.instantiate(mockedType);
        MockitoMethodInterceptor.MockAccess mockAccess = (MockitoMethodInterceptor.MockAccess) mock;
        mockAccess.setMockitoInterceptor(new MockitoMethodInterceptor(asInternalMockHandler(handler), settings));
        return mock;
    }

    public <T> Class<? extends T> getOrMakeMock(Class<T> mockedType, Set<Class> interfaces) {
        MockKey<T> mockKey = new MockKey<T>(mockedType, interfaces);
        Class<? extends T> mockType = lookup(mockKey);
        if (mockType == null) {
            synchronized (mockedType) {
                mockType = lookup(mockKey);
                if (mockType != null) {
                    return mockType;
                }
                try {
                    mockType = new ByteBuddyMockBytecodeGenerator().generateMockClass(mockedType, interfaces);
                } catch (Exception generationFailed) {
                    prettifyFailure(mockedType, generationFailed);
                }
                PREVIOUSLY_GENERATED_MOCK_CLASSES.put(mockKey, mockType);
            }
        }
        return mockType;
    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T> lookup(MockKey<T> mockKey) {
        return (Class<? extends T>) PREVIOUSLY_GENERATED_MOCK_CLASSES.get(mockKey);
    }

    private static void prettifyFailure(Class<?> mockedType, Exception generationFailed) {
        if (Modifier.isPrivate(mockedType.getModifiers())) {
            throw new MockitoException(join(
                    "Mockito cannot mock this class: " + mockedType,
                    ".",
                    "Most likely it is a private class that is not visible by Mockito"
            ));
        }
        throw new MockitoException(join(
                "Mockito cannot mock this class: " + mockedType,
                "",
                "Mockito can only mock visible & non-final classes.",
                "If you're not sure why you're getting this error, please report to the mailing list."),
                generationFailed
        );
    }

    public MockHandler getHandler(Object mock) {
        if (!(mock instanceof MockitoMethodInterceptor.MockAccess)) {
            return null;
        }
        return ((MockitoMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler();
    }

    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MockitoMethodInterceptor.MockAccess) mock).setMockitoInterceptor(new MockitoMethodInterceptor(asInternalMockHandler(newHandler), settings));
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


    private static interface ClassInstantiator {

        <T> T instantiate(Class<T> type);

        static class UsingObjenesis implements ClassInstantiator {

            private final Objenesis objenesis;

            public UsingObjenesis(boolean useClassCache) {
                this.objenesis = new ObjenesisStd(useClassCache);
            }

            public <T> T instantiate(Class<T> type) {
                return objenesis.newInstance(type);
            }
        }
    }

    private static class MockKey<T> {

        private final Class<T> mockedType;
        private final Set<Class> types;

        private MockKey(Class<T> mockedType, Set<Class> interfaces) {
            this.mockedType = mockedType;
            this.types = new HashSet<Class>(interfaces);
            types.add(mockedType);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;

            MockKey mockKey = (MockKey<?>) other;

            if (!mockedType.equals(mockKey.mockedType)) return false;
            if (!types.equals(mockKey.types)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = mockedType.hashCode();
            result = 31 * result + types.hashCode();
            return result;
        }
    }
}
