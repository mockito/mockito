/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import org.mockito.InjectUnsafe;

import java.lang.annotation.Annotation;

/**
 * A neutral element implementation of {@link InjectUnsafe} so we don't have to deal with null.
 */
@SuppressWarnings("ClassExplicitlyAnnotation")
class InjectUnsafeFallback implements InjectUnsafe {

    @Override
    public UnsafeFieldModifier value() {
        return InjectUnsafe.FALLBACK_VALUE;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return InjectUnsafe.class;
    }
}
