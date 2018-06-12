/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import org.mockito.InjectUnsafe;

import java.lang.annotation.Annotation;

class InjectUnsafeDefaults implements InjectUnsafe {
    @Override
    public Class<? extends Annotation> annotationType() {
        return InjectUnsafe.class;
    }

    @Override
    public OverrideInstanceFields instanceFields() {
        return OverrideInstanceFields.NONE;
    }

    @Override
    public OverrideStaticFields staticFields() {
        return OverrideStaticFields.NONE;
    }
}
