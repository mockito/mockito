/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.TypeCache;
import org.mockito.mock.SerializableMode;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.Callable;

class TypeCachingBytecodeGenerator extends ReferenceQueue<ClassLoader> implements BytecodeGenerator {

    private final Object BOOTSTRAP_LOCK = new Object();

    private final BytecodeGenerator bytecodeGenerator;

    private final TypeCache<SerializationFeatureKey> typeCache;

    public TypeCachingBytecodeGenerator(BytecodeGenerator bytecodeGenerator, boolean weak) {
        this.bytecodeGenerator = bytecodeGenerator;
        typeCache = new TypeCache.WithInlineExpunction<SerializationFeatureKey>(weak ? TypeCache.Sort.WEAK : TypeCache.Sort.SOFT);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<T> mockClass(final MockFeatures<T> params) {
        try {
            ClassLoader classLoader = params.mockedType.getClassLoader();
            return (Class<T>) typeCache.findOrInsert(classLoader,
                    new SerializationFeatureKey(params.mockedType, params.interfaces, params.serializableMode),
                    new Callable<Class<?>>() {
                        @Override
                        public Class<?> call() throws Exception {
                            return bytecodeGenerator.mockClass(params);
                        }
                    }, classLoader == null ? BOOTSTRAP_LOCK : classLoader);
        } catch (IllegalArgumentException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw exception;
            }
        }
    }

    private static class SerializationFeatureKey extends TypeCache.SimpleKey {

        private final SerializableMode serializableMode;

        private SerializationFeatureKey(Class<?> type, Set<Class<?>> additionalType, SerializableMode serializableMode) {
            super(type, additionalType);
            this.serializableMode = serializableMode;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            if (!super.equals(object)) return false;
            SerializationFeatureKey that = (SerializationFeatureKey) object;
            return serializableMode.equals(that.serializableMode);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + serializableMode.hashCode();
            return result;
        }
    }
}
