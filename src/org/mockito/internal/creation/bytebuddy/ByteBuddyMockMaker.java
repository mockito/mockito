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

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ByteBuddyMockMaker implements MockMaker {

    private static interface SilentConstructor {

        <T> T instantiate(Class<T> type);

        static class UsingObjenesis implements SilentConstructor {

            private final Objenesis objenesis;

            public UsingObjenesis(Objenesis objenesis) {
                this.objenesis = objenesis;
            }

            public <T> T instantiate(Class<T> type) {
                return objenesis.newInstance(type);
            }
        }

        static enum Unavailable implements SilentConstructor {

            INSTANCE;

            public <T> T instantiate(Class<T> type) {
                throw new MockitoException("Could not create mock: Objenesis is missing on the class path");
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

    private final SilentConstructor silentConstructor;

    private static final Map<MockKey<?>, Class<?>> PREVIOUS_CLASSES = new ConcurrentHashMap<MockKey<?>, Class<?>>();


    public ByteBuddyMockMaker() {
        silentConstructor = makeSilentConstructor();
    }

    private static SilentConstructor makeSilentConstructor() {
        try {
            return new SilentConstructor.UsingObjenesis(new ObjenesisStd(new GlobalConfiguration().enableClassCache()));
        } catch (Throwable throwable) {
            return SilentConstructor.Unavailable.INSTANCE;
        }
    }

    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        if(settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
            throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddy");
        }
        Class<? extends T> mockedType = getOrMakeMock(
                settings.getTypeToMock(),
                settings.getExtraInterfaces()
                );
        T mock = silentConstructor.instantiate(mockedType);
        MethodInterceptor.Access access = (MethodInterceptor.Access) mock;
        access.setMockitoInterceptor(new MethodInterceptor(asInternalMockHandler(handler), settings));
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
                } catch (Exception e) {
                    prettify(mockedType, e);
                }
                PREVIOUS_CLASSES.put(mockKey, mockType);
            }
        }
        return mockType;
    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T> lookup(MockKey<T> mockKey) {
        return (Class<? extends T>) PREVIOUS_CLASSES.get(mockKey);
    }

    private static void prettify(Class<?> mockedType, Exception e) {
        if (Modifier.isPrivate(mockedType.getModifiers())) {
            throw new MockitoException("\n"
                    + "Mockito cannot mock this class: " + mockedType
                    + ".\n"
                    + "Most likely it is a private class that is not visible by Mockito");
        }
        throw new MockitoException("\n"
                + "Mockito cannot mock this class: " + mockedType
                + "\n"
                + "Mockito can only mock visible & non-final classes."
                + "\n"
                + "If you're not sure why you're getting this error, please report to the mailing list.", e);
    }

    public MockHandler getHandler(Object mock) {
        if (!(mock instanceof MethodInterceptor.Access)) {
            return null;
        }
        return ((MethodInterceptor.Access) mock).getMockitoInterceptor().getMockHandler();
    }

    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MethodInterceptor.Access) mock).setMockitoInterceptor(new MethodInterceptor(asInternalMockHandler(newHandler), settings));
    }

    private static InternalMockHandler asInternalMockHandler(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException("At the moment you cannot provide own implementations of MockHandler." +
                    "\nPlease see the javadocs for the MockMaker interface.");
        }
        return (InternalMockHandler) handler;
    }

}
