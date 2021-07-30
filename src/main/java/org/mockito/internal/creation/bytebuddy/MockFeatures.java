/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.util.Collections;
import java.util.Set;

import org.mockito.mock.SerializableMode;
import org.mockito.stubbing.Answer;

class MockFeatures<T> {

    final Class<T> mockedType;
    final Set<Class<?>> interfaces;
    final SerializableMode serializableMode;
    final boolean stripAnnotations;
    final Answer defaultAnswer;

    private MockFeatures(
            Class<T> mockedType,
            Set<Class<?>> interfaces,
            SerializableMode serializableMode,
            boolean stripAnnotations,
            Answer defaultAnswer) {
        this.mockedType = mockedType;
        this.interfaces = Collections.unmodifiableSet(interfaces);
        this.serializableMode = serializableMode;
        this.stripAnnotations = stripAnnotations;
        this.defaultAnswer = defaultAnswer;
    }

    public static <T> MockFeatures<T> withMockFeatures(
            Class<T> mockedType,
            Set<Class<?>> interfaces,
            SerializableMode serializableMode,
            boolean stripAnnotations,
            Answer defaultAnswer) {
        return new MockFeatures<T>(
                mockedType, interfaces, serializableMode, stripAnnotations, defaultAnswer);
    }
}
