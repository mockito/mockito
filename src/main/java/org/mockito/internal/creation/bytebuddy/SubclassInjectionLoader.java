/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.Platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.internal.util.StringUtil.join;

class SubclassInjectionLoader implements SubclassLoader {

    private static final String ERROR_MESSAGE = join("The current JVM does not support any class injection mechanism.",
        "",
        "Currently, Mockito supports injection via neither by method handle lookups or using sun.misc.Unsafe",
        "Neither seems to be available on your current JVM.");

    private final SubclassLoader loader;

    SubclassInjectionLoader() {
        if (!Boolean.getBoolean("org.mockito.internal.noUnsafeInjection") && ClassInjector.UsingReflection.isAvailable()) {
            this.loader = new WithReflection();
        } else if (ClassInjector.UsingLookup.isAvailable()) {
            this.loader = tryLookup();
        } else {
            throw new MockitoException(join(ERROR_MESSAGE, "", Platform.describe()));
        }
    }

    private static SubclassLoader tryLookup() {
        try {
            Class<?> methodHandles = Class.forName("java.lang.invoke.MethodHandles");
            Object lookup = methodHandles.getMethod("lookup").invoke(null);
            Method privateLookupIn = methodHandles.getMethod("privateLookupIn", Class.class, Class.forName("java.lang.invoke.MethodHandles$Lookup"));
            Object codegenLookup = privateLookupIn.invoke(null, InjectionBase.class, lookup);
            return new WithLookup(lookup, codegenLookup, privateLookupIn);
        } catch (Exception exception) {
            throw new MockitoException(join(ERROR_MESSAGE, "", Platform.describe()), exception);
        }
    }

    private static class WithReflection implements SubclassLoader {

        @Override
        public boolean isDisrespectingOpenness() {
            return true;
        }

        @Override
        public ClassLoadingStrategy<ClassLoader> resolveStrategy(Class<?> mockedType, ClassLoader classLoader, boolean localMock) {
            return ClassLoadingStrategy.Default.INJECTION.with(localMock ? mockedType.getProtectionDomain() : InjectionBase.class.getProtectionDomain());
        }
    }

    private static class WithLookup implements SubclassLoader {

        private final Object lookup;

        private final Object codegenLookup;

        private final Method privateLookupIn;

        WithLookup(Object lookup, Object codegenLookup, Method privateLookupIn) {
            this.lookup = lookup;
            this.codegenLookup = codegenLookup;
            this.privateLookupIn = privateLookupIn;
        }

        @Override
        public boolean isDisrespectingOpenness() {
            return false;
        }

        @Override
        public ClassLoadingStrategy<ClassLoader> resolveStrategy(Class<?> mockedType, ClassLoader classLoader, boolean localMock) {
            if (localMock) {
                try {
                    Object privateLookup;
                    try {
                        privateLookup = privateLookupIn.invoke(null, mockedType, lookup);
                    } catch (InvocationTargetException exception) {
                        if (exception.getCause() instanceof IllegalAccessException) {
                            return ClassLoadingStrategy.Default.WRAPPER.with(mockedType.getProtectionDomain());
                        } else {
                            throw exception.getCause();
                        }
                    }
                    return ClassLoadingStrategy.UsingLookup.of(privateLookup);
                } catch (Throwable exception) {
                    throw new MockitoException(join(
                        "The Java module system prevents Mockito from defining a mock class in the same package as " + mockedType,
                        "",
                        "To overcome this, you must open and export the mocked type to Mockito.",
                        "Remember that you can also do so programmatically if the mocked class is defined by the same module as your test code",
                        exception
                    ));
                }
            } else if (classLoader == InjectionBase.class.getClassLoader()) {
                return ClassLoadingStrategy.UsingLookup.of(codegenLookup);
            } else {
                return ClassLoadingStrategy.Default.WRAPPER.with(mockedType.getProtectionDomain());
            }
        }
    }

    @Override
    public boolean isDisrespectingOpenness() {
        return loader.isDisrespectingOpenness();
    }

    @Override
    public ClassLoadingStrategy<ClassLoader> resolveStrategy(Class<?> mockedType, ClassLoader classLoader, boolean localMock) {
        return loader.resolveStrategy(mockedType, classLoader, localMock);
    }
}
