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
    private final CachingBytecodeGenerator cachingBytecodeGenerator;

//    // TODO should be weakhashmap
//    private static final Map<MockKey<?>, Class<?>> PREVIOUSLY_GENERATED_MOCK_CLASSES = new ConcurrentHashMap<MockKey<?>, Class<?>>();


    public ByteBuddyMockMaker() {
        classInstantiator = initializeClassInstantiator();
        cachingBytecodeGenerator = new CachingBytecodeGenerator();
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

    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
            throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddy");
        }
        Class<? extends T> mockedType = cachingBytecodeGenerator.get(
                settings.getTypeToMock(),
                settings.getExtraInterfaces()
        );
        T mock = classInstantiator.instantiate(mockedType);
        MockitoMethodInterceptor.MockAccess mockAccess = (MockitoMethodInterceptor.MockAccess) mock;
        mockAccess.setMockitoInterceptor(new MockitoMethodInterceptor(asInternalMockHandler(handler), settings));

        return settings.getTypeToMock().cast(mock);
    }

//    public <T> Class<? extends T> getOrMakeMock(Class<T> mockedType, Set<Class> interfaces) {
//        MockKey<T> mockKey = new MockKey<T>(mockedType, interfaces);
//        Class<? extends T> mockType = lookup(mockKey);
//        if (mockType == null) {
//            synchronized (mockedType) {
//                mockType = lookup(mockKey);
//                if (mockType != null) {
//                    return mockType;
//                }
//                try {
//                    mockType = new ByteBuddyMockBytecodeGenerator().generateMockClass(mockedType, interfaces);
//                } catch (Exception generationFailed) {
//                    prettifyFailure(mockedType, generationFailed);
//                }
//                PREVIOUSLY_GENERATED_MOCK_CLASSES.put(mockKey, mockType);
//            }
//        }
////        System.out.println(mockedType.getSimpleName() + ";" + mockedType.getClassLoader() + "   --->   " + mockType.getSimpleName() + ";" + mockType.getClassLoader());
//        System.out.println(PREVIOUSLY_GENERATED_MOCK_CLASSES.size() + " -> " + PREVIOUSLY_GENERATED_MOCK_CLASSES);
//        return mockType;
//    }
//
//    @SuppressWarnings("unchecked")
//    private <T> Class<? extends T> lookup(MockKey<T> mockKey) {
//        return (Class<? extends T>) PREVIOUSLY_GENERATED_MOCK_CLASSES.get(mockKey);
//    }


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


}
