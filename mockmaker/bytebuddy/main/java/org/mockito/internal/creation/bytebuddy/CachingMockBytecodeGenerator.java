package org.mockito.internal.creation.bytebuddy;

import static org.mockito.internal.util.StringJoiner.join;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.mockito.exceptions.base.MockitoException;

class CachingMockBytecodeGenerator {

    private final Lock avoidingClassLeakCacheLock = new ReentrantLock();
    public final WeakHashMap<ClassLoader, CachedBytecodeGenerator> avoidingClassLeakageCache =
            new WeakHashMap<ClassLoader, CachedBytecodeGenerator>();

    private final MockBytecodeGenerator mockBytecodeGenerator = new MockBytecodeGenerator();

    public <T> Class<T> get(MockFeatures<T> params) {
        // TODO improves locking behavior with ReentrantReadWriteLock ?
        avoidingClassLeakCacheLock.lock();
        try {

            Class generatedMockClass = mockCachePerClassLoaderOf(params.mockedType).getOrGenerateMockClass(
                    params
            );

            return generatedMockClass;
        } finally {
          avoidingClassLeakCacheLock.unlock();
        }
    }

    private <T> CachedBytecodeGenerator mockCachePerClassLoaderOf(Class<T> mockedType) {
        if (!avoidingClassLeakageCache.containsKey(mockedType.getClassLoader())) {
            avoidingClassLeakageCache.put(
                    mockedType.getClassLoader(),
                    new CachedBytecodeGenerator(mockBytecodeGenerator)
            );
        }
        return avoidingClassLeakageCache.get(mockedType.getClassLoader());
    }

    private static class CachedBytecodeGenerator {
        private ConcurrentHashMap<MockKey, WeakReference<Class>> generatedClassCache =
                new ConcurrentHashMap<MockKey, WeakReference<Class>>();
        private final MockBytecodeGenerator generator;

        private CachedBytecodeGenerator(MockBytecodeGenerator generator) {
            this.generator = generator;
        }

        public <T> Class getOrGenerateMockClass(MockFeatures<T> features) {
            MockKey mockKey = MockKey.of(features.mockedType, features.interfaces);
            Class generatedMockClass = null;
            WeakReference<Class> classWeakReference = generatedClassCache.get(mockKey);
            if(classWeakReference != null) {
                generatedMockClass = classWeakReference.get();
            }
            if(generatedMockClass == null) {
                generatedMockClass = generate(features);
            }
            generatedClassCache.put(mockKey, new WeakReference<Class>(generatedMockClass));
            return generatedMockClass;
        }

        private <T> Class<? extends T> generate(MockFeatures<T> mockFeatures) {
            try {
                return generator.generateMockClass(mockFeatures);
            } catch (Exception bytecodeGenerationFailed) {
                throw prettifyFailure(mockFeatures, bytecodeGenerationFailed);
            }
        }

        private RuntimeException prettifyFailure(MockFeatures<?> mockFeatures, Exception generationFailed) {
            if (Modifier.isPrivate(mockFeatures.mockedType.getModifiers())) {
                throw new MockitoException(join(
                        "Mockito cannot mock this class: " + mockFeatures.mockedType + ".",
                        "Most likely it is a private class that is not visible by Mockito",
                        ""
                ), generationFailed);
            }
            throw new MockitoException(join(
                    "Mockito cannot mock this class: " + mockFeatures.mockedType,
                    "",
                    "Mockito can only mock visible & non-final classes.",
                    "If you're not sure why you're getting this error, please report to the mailing list.",
                    "",
                    "Underlying exception : " + generationFailed),
                    generationFailed
            );
        }

        // should be stored as a weak reference
        private static class MockKey<T> {
            private final String mockedType;
            private final Set<String> types = new HashSet<String>();

            private MockKey(Class<T> mockedType, Set<Class> interfaces) {
                this.mockedType = mockedType.getName();
                for (Class anInterface : interfaces) {
                    types.add(anInterface.getName());
                }
                types.add(this.mockedType);
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

            public static <T> MockKey of(Class<T> mockedType, Set<Class> interfaces) {
                return new MockKey<T>(mockedType, interfaces);
            }
        }
    }
}
