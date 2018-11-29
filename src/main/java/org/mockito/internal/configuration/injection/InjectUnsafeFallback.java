/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import java.lang.annotation.Annotation;

import org.mockito.InjectUnsafe;

import static org.mockito.InjectUnsafe.UnsafeFieldModifier.NONE;

/**
 * A neutral element implementation of {@link InjectUnsafe} so we don't have to deal with null.
 */
@SuppressWarnings("ClassExplicitlyAnnotation")
class InjectUnsafeFallback implements InjectUnsafe {

    @Override
    public UnsafeFieldModifier[] allow() {
        return new UnsafeFieldModifier[] { NONE };
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return InjectUnsafe.class;
    }

}
