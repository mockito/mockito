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

public class CachingBytecodeGenerator {

    public final WeakHashMap<ClassLoader, ConcurrentHashMap<MockKey, WeakReference<Class>>> avoidingClassLeakCache =
            new WeakHashMap<ClassLoader, ConcurrentHashMap<MockKey, WeakReference<Class>>>();

    private final ByteBuddyMockBytecodeGenerator byteBuddyMockBytecodeGenerator = new ByteBuddyMockBytecodeGenerator();

    public final Lock avoidingClassLeakCacheLock = new ReentrantLock();

    public <T> Class<? extends T> get(final Class<T> mockedType, final Set<Class> interfaces) {
        avoidingClassLeakCacheLock.lock();
        try {

            ConcurrentHashMap<MockKey, WeakReference<Class>> mockCache = mockCachePerClassLoaderOf(mockedType);
            Class generatedMockClass = getOrGenerateMockClass(
                    mockCache,
                    mockedType,
                    interfaces
            );

            return generatedMockClass;
        } finally {
          avoidingClassLeakCacheLock.unlock();
        }
    }

    private <T> Class getOrGenerateMockClass(ConcurrentHashMap<MockKey, WeakReference<Class>> mockCache, Class<T> mockedType, Set<Class> interfaces) {
        MockKey mockKey = MockKey.of(mockedType, interfaces);
        Class generatedMockClass = null;
        WeakReference<Class> classWeakReference = mockCache.get(mockKey);
        if(classWeakReference != null) {
            generatedMockClass = classWeakReference.get();
        }
        if(generatedMockClass == null) {
             generatedMockClass = generateMockClass(mockedType, interfaces);
        }
        mockCache.put(mockKey, new WeakReference<Class>(generatedMockClass));
        return generatedMockClass;
    }

    private <T> ConcurrentHashMap<MockKey, WeakReference<Class>> mockCachePerClassLoaderOf(Class<T> mockedType) {
        if (!avoidingClassLeakCache.containsKey(mockedType.getClassLoader())) {
            avoidingClassLeakCache.put(
                    mockedType.getClassLoader(),
                    new ConcurrentHashMap<MockKey, WeakReference<Class>>()
            );
        }
        return avoidingClassLeakCache.get(mockedType.getClassLoader());
    }

    private <T> Class<? extends T> generateMockClass(Class<T> mockedType, Set<Class> interfaces) {
        try {
            return byteBuddyMockBytecodeGenerator.generateMockClass(mockedType, interfaces);
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(mockedType, bytecodeGenerationFailed);
        }
    }

    private static RuntimeException prettifyFailure(Class<?> mockedType, Exception generationFailed) {
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


    private interface ClassloderBytecodeGenerator {
        <T> Class<T> generated();
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
