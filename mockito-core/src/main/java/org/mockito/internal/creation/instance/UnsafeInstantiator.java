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
