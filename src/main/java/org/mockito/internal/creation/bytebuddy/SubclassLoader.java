/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

public interface SubclassLoader {

    ClassLoadingStrategy<ClassLoader> getStrategy(Class<?> mockedType);

}
