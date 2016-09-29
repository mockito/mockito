package org.mockito.internal.creation.bytebuddy;

public interface BytecodeGenerator {

    <T> Class<? extends T> mockClass(MockFeatures<T> features);
}
