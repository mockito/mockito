/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class TypeCachingBytecodeGenerator extends ReferenceQueue<ClassLoader> implements BytecodeGenerator {

    private static final ClassLoader BOOT_LOADER = new URLClassLoader(new URL[0], TypeCachingBytecodeGenerator.class.getClassLoader());

    final ConcurrentMap<Key, CachedBytecodeGenerator> avoidingClassLeakageCache = new ConcurrentHashMap<Key, CachedBytecodeGenerator>();

    private final BytecodeGenerator bytecodeGenerator;

    private final boolean weak;

    public TypeCachingBytecodeGenerator(BytecodeGenerator bytecodeGenerator, boolean weak) {
        this.bytecodeGenerator = bytecodeGenerator;
        this.weak = weak;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<T> mockClass(MockFeatures<T> params) {
        cleanUpCachesForObsoleteClassLoaders();
        return (Class<T>) mockCachePerClassLoaderOf(params.mockedType.getClassLoader()).getOrGenerateMockClass(params);
    }

    void cleanUpCachesForObsoleteClassLoaders() {
        Reference<?> reference;
        // Any weak key that is used in the map is enqueued to this reference queue once a class loader is no longer reachable.
        while ((reference = poll()) != null) {
            avoidingClassLeakageCache.remove(reference);
        }
    }

    private CachedBytecodeGenerator mockCachePerClassLoaderOf(ClassLoader classLoader) {
        classLoader = classLoader == null ? BOOT_LOADER : classLoader;
        CachedBytecodeGenerator generator = avoidingClassLeakageCache.get(new LookupKey(classLoader));
        if (generator == null) {
            CachedBytecodeGenerator newGenerator = new CachedBytecodeGenerator(bytecodeGenerator, weak);
            generator = avoidingClassLeakageCache.putIfAbsent(new WeakKey(classLoader, this), newGenerator);
            if (generator == null) {
                generator = newGenerator;
            }
        }
        return generator;
    }

    private static class CachedBytecodeGenerator {

        private ConcurrentHashMap<MockKey, Reference<Class<?>>> generatedClassCache = new ConcurrentHashMap<MockKey, Reference<Class<?>>>();

        private BytecodeGenerator bytecodeGenerator;

        private final boolean weak;

        private CachedBytecodeGenerator(BytecodeGenerator bytecodeGenerator, boolean weak) {
            this.bytecodeGenerator = bytecodeGenerator;
            this.weak = weak;
        }

        private Class<?> getMockClass(MockKey<?> mockKey) {
            Reference<Class<?>> classReference = generatedClassCache.get(mockKey);
            if (classReference != null) {
                return classReference.get();
            } else {
                return null;
            }
        }

        Class<?> getOrGenerateMockClass(MockFeatures<?> features) {
            MockKey<?> mockKey = MockKey.of(features.mockedType, features.interfaces);
            Class<?> generatedMockClass = getMockClass(mockKey);
            if (generatedMockClass == null) {
                synchronized (features.mockedType) {
                    generatedMockClass = getMockClass(mockKey);
                    if (generatedMockClass == null) {
                        generatedMockClass = bytecodeGenerator.mockClass(features);
                        generatedClassCache.put(mockKey, weak ? new WeakReference<Class<?>>(generatedMockClass) : new SoftReference<Class<?>>(generatedMockClass));
                    }
                }
            }
            return generatedMockClass;
        }

        // should be stored as a weak reference
        private static class MockKey<T> {

            private final String mockedType;

            private final Set<String> types;

            private MockKey(Class<T> mockedType, Set<Class<?>> interfaces) {
                this.mockedType = mockedType.getName();
                if (interfaces.isEmpty()) { // Optimize memory footprint for the common case.
                    types = Collections.emptySet();
                } else {
                    types = new HashSet<String>();
                    for (Class<?> anInterface : interfaces) {
                        types.add(anInterface.getName());
                    }
                    types.add(this.mockedType);
                }
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

            public static <T> MockKey<T> of(Class<T> mockedType, Set<Class<?>> interfaces) {
                return new MockKey<T>(mockedType, interfaces);
            }
        }
    }

    private interface Key {

        ClassLoader get();
    }

    private static class LookupKey implements Key {

        private final ClassLoader value;

        private final int hashCode;

        public LookupKey(ClassLoader value) {
            this.value = value;
            hashCode = System.identityHashCode(value);
        }

        @Override
        public ClassLoader get() {
            return value;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Key)) return false;
            return value == ((Key) object).get();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

    private static class WeakKey extends WeakReference<ClassLoader> implements Key {

        private final int hashCode;

        public WeakKey(ClassLoader referent, ReferenceQueue<ClassLoader> q) {
            super(referent, q);
            hashCode = System.identityHashCode(referent);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Key)) return false;
            return get() == ((Key) object).get();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
