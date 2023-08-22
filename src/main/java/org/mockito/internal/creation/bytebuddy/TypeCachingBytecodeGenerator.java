/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.bytebuddy.TypeCache;
import org.mockito.mock.SerializableMode;

class TypeCachingBytecodeGenerator extends ReferenceQueue<ClassLoader>
        implements BytecodeGenerator {
    /**
     * The size of the {@link #cacheLocks}.
     * <p>Caution: This must match the {@link #CACHE_LOCK_MASK}.
     */
    private static final int CACHE_LOCK_SIZE = 16;

    /**
     * The mask to use to mask out the {@link MockitoMockKey#hashCode()} to find the {@link #cacheLocks}.
     * <p>Caution: this must match the bits of the {@link #CACHE_LOCK_SIZE}.
     */
    private static final int CACHE_LOCK_MASK = 0x0F;

    private final BytecodeGenerator bytecodeGenerator;

    private final TypeCache<MockitoMockKey> typeCache;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * This array contains {@link TypeCachingLock} instances, which are used as java monitor locks for
     * {@link TypeCache#findOrInsert(ClassLoader, Object, Callable, Object)}.
     * The locks spread the lock to acquire over multiple locks instead of using a single lock
     * {@code BOOTSTRAP_LOCK} for all {@link MockitoMockKey}.
     *
     * <p>Note: We can't simply use the mockedType class lock as a lock,
     * because the {@link MockitoMockKey}, will be the same for different mockTypes + interfaces.
     *
     * <p>#3035: Excessive locking in TypeCachingBytecodeGenerator#BOOTSTRAP_LOCK
     */
    private final TypeCachingLock[] cacheLocks;

    public TypeCachingBytecodeGenerator(BytecodeGenerator bytecodeGenerator, boolean weak) {
        this.bytecodeGenerator = bytecodeGenerator;
        typeCache =
                new TypeCache.WithInlineExpunction<>(
                        weak ? TypeCache.Sort.WEAK : TypeCache.Sort.SOFT);

        this.cacheLocks = new TypeCachingLock[CACHE_LOCK_SIZE];
        for (int i = 0; i < CACHE_LOCK_SIZE; i++) {
            cacheLocks[i] = new TypeCachingLock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<T> mockClass(final MockFeatures<T> params) {
        lock.readLock().lock();
        try {
            Class<T> mockedType = params.mockedType;
            ClassLoader classLoader = mockedType.getClassLoader();
            MockitoMockKey key =
                    new MockitoMockKey(
                            mockedType,
                            params.interfaces,
                            params.serializableMode,
                            params.stripAnnotations);
            return (Class<T>)
                    typeCache.findOrInsert(
                            classLoader,
                            key,
                            () -> bytecodeGenerator.mockClass(params),
                            getCacheLockForKey(key));
        } catch (IllegalArgumentException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw exception;
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Returns a {@link TypeCachingLock}, which locks the {@link TypeCache#findOrInsert(ClassLoader, Object, Callable, Object)}.
     *
     * @param key the key to lock
     * @return the {@link TypeCachingLock} to use to lock the {@link TypeCache}
     */
    private TypeCachingLock getCacheLockForKey(MockitoMockKey key) {
        int hashCode = key.hashCode();
        // Try to spread some higher bits with XOR to lower bits, because we only use lower bits.
        hashCode = hashCode ^ (hashCode >>> 16);
        int index = hashCode & CACHE_LOCK_MASK;
        return cacheLocks[index];
    }

    @Override
    public void mockClassStatic(Class<?> type) {
        bytecodeGenerator.mockClassStatic(type);
    }

    @Override
    public void mockClassConstruction(Class<?> type) {
        bytecodeGenerator.mockClassConstruction(type);
    }

    @Override
    public void clearAllCaches() {
        lock.writeLock().lock();
        try {
            typeCache.clear();
            bytecodeGenerator.clearAllCaches();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private static final class TypeCachingLock {}

    private static class MockitoMockKey extends TypeCache.SimpleKey {

        private final SerializableMode serializableMode;
        private final boolean stripAnnotations;

        private MockitoMockKey(
                Class<?> type,
                Set<Class<?>> additionalType,
                SerializableMode serializableMode,
                boolean stripAnnotations) {
            super(type, additionalType);
            this.serializableMode = serializableMode;
            this.stripAnnotations = stripAnnotations;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            if (!super.equals(object)) {
                return false;
            }
            MockitoMockKey that = (MockitoMockKey) object;
            return stripAnnotations == that.stripAnnotations
                    && serializableMode.equals(that.serializableMode);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (stripAnnotations ? 1 : 0);
            result = 31 * result + serializableMode.hashCode();
            return result;
        }
    }
}
