package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

class SubclassInjectionLoader implements SubclassLoader {

    @Override
    public ClassLoadingStrategy<ClassLoader> getStrategy(Class<?> mockedType) {
        return ClassLoadingStrategy.Default.INJECTION.with(mockedType.getProtectionDomain());
    }
}
