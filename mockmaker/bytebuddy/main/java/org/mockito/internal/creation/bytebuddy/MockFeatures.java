package org.mockito.internal.creation.bytebuddy;

import java.util.Collections;
import java.util.Set;

class MockFeatures<T> {
    final Class<T> mockedType;
    final Set<Class<?>> interfaces;
    final boolean crossClassLoaderSerializable;

    private MockFeatures(Class<T> mockedType, Set<Class<?>> interfaces, boolean crossClassLoaderSerializable) {
        this.mockedType = mockedType;
        this.interfaces = Collections.unmodifiableSet(interfaces);
        this.crossClassLoaderSerializable = crossClassLoaderSerializable;
    }

    public static <T> MockFeatures<T> withMockFeatures(Class<T> mockedType, Set<Class<?>> interfaces, boolean crossClassLoaderSerializable) {
        return new MockFeatures<T>(mockedType, interfaces, crossClassLoaderSerializable);
    }
}
