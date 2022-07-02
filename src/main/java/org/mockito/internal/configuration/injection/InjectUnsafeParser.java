/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import org.mockito.InjectUnsafe;

import java.lang.reflect.Field;

class InjectUnsafeParser {
    public InjectUnsafe parse(Field field) {
        InjectUnsafe injectUnsafe = field.getAnnotation(InjectUnsafe.class);
        if (injectUnsafe == null) {
            injectUnsafe = new InjectUnsafeFallback();
        }

        return injectUnsafe;
    }
}
