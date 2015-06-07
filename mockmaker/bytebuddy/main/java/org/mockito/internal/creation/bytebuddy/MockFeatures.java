package org.mockito.internal.creation.bytebuddy;

import java.util.Collections;
import java.util.Set;

@SuppressWarnings("rawtypes")
class MockFeatures<T> {
    final Class<T> mockedType;
    final Set<Class> interfaces;
    final boolean crossClassLoaderSerializable;

    private MockFeatures(final Class<T> mockedType, final Set<Class> interfaces, final boolean crossClassLoaderSerializable) {
        this.mockedType = mockedType;
        this.interfaces = Collections.unmodifiableSet(interfaces);
        this.crossClassLoaderSerializable = crossClassLoaderSerializable;
    }

    public static <T> MockFeatures<T> withMockFeatures(final Class<T> mockedType, final Set<Class> interfaces, final boolean crossClassLoaderSerializable) {
        return new MockFeatures<T>(mockedType, interfaces, crossClassLoaderSerializable);
    }
}
