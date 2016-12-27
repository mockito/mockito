package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

public interface SubclassLoader {

    ClassLoadingStrategy getStrategy(Class<?> mockedType);

}
