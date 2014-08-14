package org.mockito.internal.creation.bytebuddy;

import org.mockito.exceptions.base.MockitoException;

import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.mockito.internal.util.StringJoiner.join;

public class CachingMockBytecodeGenerator {

    private final Lock avoidingClassLeakCacheLock = new ReentrantLock();
    public final WeakHashMap<ClassLoader, CachedBytecodeGenerator> avoidingClassLeakageCache =
            new WeakHashMap<ClassLoader, CachedBytecodeGenerator>();

    private final MockBytecodeGenerator mockBytecodeGenerator = new MockBytecodeGenerator();

    public <T> Class<? extends T> get(final Class<T> mockedType, final Set<Class> interfaces) {
        avoidingClassLeakCacheLock.lock();
        try {

            Class generatedMockClass = mockCachePerClassLoaderOf(mockedType).getOrGenerateMockClass(
                    mockedType,
                    interfaces
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

        public <T> Class getOrGenerateMockClass(Class<T> mockedType, Set<Class> interfaces) {
            MockKey mockKey = MockKey.of(mockedType, interfaces);
            Class generatedMockClass = null;
            WeakReference<Class> classWeakReference = generatedClassCache.get(mockKey);
            if(classWeakReference != null) {
                generatedMockClass = classWeakReference.get();
            }
            if(generatedMockClass == null) {
                generatedMockClass = generate(mockedType, interfaces);
            }
            generatedClassCache.put(mockKey, new WeakReference<Class>(generatedMockClass));
            return generatedMockClass;
        }

        private <T> Class<? extends T> generate(Class<T> mockedType, Set<Class> interfaces) {
            try {
                return generator.generateMockClass(mockedType, interfaces);
            } catch (Exception bytecodeGenerationFailed) {
                throw prettifyFailure(mockedType, bytecodeGenerationFailed);
            }
        }

        private RuntimeException prettifyFailure(Class<?> mockedType, Exception generationFailed) {
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

        private static class MockKey<T> {
            // weakreference
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
