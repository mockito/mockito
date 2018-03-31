/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.mock.SerializableMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.internal.util.StringUtil.join;

class SubclassInjectionLoader implements SubclassLoader {

    private final boolean disableReflection;

    private final Object lookup, codegenLookup;

    private final Method privateLookupIn;

    public SubclassInjectionLoader() {
        disableReflection = Boolean.getBoolean("org.mockito.reflection.disabled");
        Object lookup, codegenLookup;
        Method privateLookupIn;
        if (ClassInjector.UsingLookup.isAvailable()) {
            try {
                Class<?> methodHandles = Class.forName("java.lang.invoke.MethodHandles");
                lookup = methodHandles.getMethod("lookup").invoke(null);
                privateLookupIn = methodHandles.getMethod("privateLookupIn", Class.class, Class.forName("java.lang.invoke.MethodHandles$Lookup"));
                codegenLookup = privateLookupIn.invoke(null, InjectionBase.class, lookup);
            } catch (Exception ignored) {
                lookup = null;
                privateLookupIn = null;
                codegenLookup = null;
            }
        } else {
            lookup = null;
            privateLookupIn = null;
            codegenLookup = null;
        }
        this.lookup = lookup;
        this.privateLookupIn = privateLookupIn;
        this.codegenLookup = codegenLookup;
    }

    @Override
    public ClassLoadingStrategy<ClassLoader> resolveStrategy(Class<?> mockedType, SerializableMode serializableMode, ClassLoader classLoader, boolean codegen) {
        if (!disableReflection && ClassInjector.UsingReflection.isAvailable()) {
            return ClassLoadingStrategy.Default.INJECTION.with(mockedType.getProtectionDomain());
        } else if (ClassInjector.UsingLookup.isAvailable() && lookup != null && privateLookupIn != null && codegenLookup != null) {
            if (codegen) {
                return ClassLoadingStrategy.UsingLookup.of(codegenLookup);
            } else if (classLoader != mockedType.getClassLoader()) {
                return ClassLoadingStrategy.Default.WRAPPER.with(mockedType.getProtectionDomain());
            } else {
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
            }
        } else {
            throw new MockitoException("The current JVM does not support any class injection mechanism, neither by method handles or using sun.misc.Unsafe");
        }
    }
}
