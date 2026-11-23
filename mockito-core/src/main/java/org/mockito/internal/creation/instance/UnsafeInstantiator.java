/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import java.lang.reflect.Field;

import org.mockito.creation.instance.InstantiationException;
import org.mockito.creation.instance.Instantiator;
import org.mockito.exceptions.base.MockitoInitializationException;
import org.mockito.internal.SuppressSignatureCheck;

import sun.misc.Unsafe;

/**
 * Allocates instances without invoking constructors. In JDK 28, the two-argument
 * {@code ReflectionFactory.newConstructorForSerialization} method that Objenesis used on HotSpot
 * is disabled by default.
 *
 * <p>JDK 22 reimplemented serialization constructors with method handles that allocate through
 * {@code jdk.internal.misc.Unsafe.allocateInstance}. The {@code sun.misc.Unsafe.allocateInstance}
 * method delegates to the same allocator. See
 * <a href="https://github.com/openjdk/jdk/pull/15600">OpenJDK PR #15600</a>.
 *
 * <p>The {@code jdk.unsupported} module exports and opens {@code sun.misc}, so Mockito can access
 * {@code sun.misc.Unsafe} without additional module flags.
 *
 * <p>OpenJDK plans to retain {@code Unsafe.allocateInstance} for the medium term. See
 * <a href="https://openjdk.org/jeps/471#Future-Work:~:text=allocateInstance,project">JEP 471</a>.
 */
@SuppressSignatureCheck
final class UnsafeInstantiator implements Instantiator {

    static final Instantiator INSTANCE = new UnsafeInstantiator();

    private static final Unsafe UNSAFE = loadUnsafe();

    private UnsafeInstantiator() {}

    @Override
    public <T> T newInstance(Class<T> cls) {
        try {
            return cls.cast(UNSAFE.allocateInstance(cls));
        } catch (java.lang.InstantiationException e) {
            throw new InstantiationException("Unable to instantiate " + cls.getName(), e);
        }
    }

    private static Unsafe loadUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new MockitoInitializationException(
                    "Unable to access sun.misc.Unsafe to allocate instances without invoking constructors.",
                    e);
        }
    }
}
