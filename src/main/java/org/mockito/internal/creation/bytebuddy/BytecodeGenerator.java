/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

public interface BytecodeGenerator {

    <T> Class<? extends T> mockClass(MockFeatures<T> features);

    void mockClassStatic(Class<?> type);
}
