/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.Callable;

import net.bytebuddy.TypeCache;
import org.mockito.mock.SerializableMode;

class TypeCachingBytecodeGenerator extends ReferenceQueue<ClassLoader>
        implements BytecodeGenerator {

    private final Object BOOTSTRAP_LOCK = new Object();

    private final BytecodeGenerator bytecodeGenerator;

    private final TypeCache<MockitoMockKey> typeCache;

    public TypeCachingBytecodeGenerator(BytecodeGenerator bytecodeGenerator, boolean weak) {
        this.bytecodeGenerator = bytecodeGenerator;
        typeCache =
                new TypeCache.WithInlineExpunction<MockitoMockKey>(
                        weak ? TypeCache.Sort.WEAK : TypeCache.Sort.SOFT);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<T> mockClass(final MockFeatures<T> params) {
        try {
            ClassLoader classLoader = params.mockedType.getClassLoader();
            return (Class<T>)
                    typeCache.findOrInsert(
                            classLoader,
                            new MockitoMockKey(
                                    params.mockedType,
                                    params.interfaces,
                                    params.serializableMode,
                                    params.stripAnnotations),
                            new Callable<Class<?>>() {
                                @Override
                                public Class<?> call() throws Exception {
                                    return bytecodeGenerator.mockClass(params);
                                }
                            },
                            BOOTSTRAP_LOCK);
        } catch (IllegalArgumentException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw exception;
            }
        }
    }

    @Override
    public void mockClassStatic(Class<?> type) {
        bytecodeGenerator.mockClassStatic(type);
    }

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
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            if (!super.equals(object)) return false;
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
