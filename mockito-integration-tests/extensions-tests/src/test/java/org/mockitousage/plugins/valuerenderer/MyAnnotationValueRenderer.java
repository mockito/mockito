/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.valuerenderer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.mockito.plugins.ValueRenderer;

public class MyAnnotationValueRenderer implements ValueRenderer {
    @Retention(RetentionPolicy.RUNTIME)
    @interface SomeAnnotation {
    }

    @Override
    public int getPluginPriority() {
        return DEFAULT_PRIORITY + 1;
    }

    @Override
    public String print(Object value) {
        if (value == null) {
            return null;
        }
        if (!value.getClass().isAnnotationPresent(SomeAnnotation.class)) {
            return null;
        }
        return value.getClass().getSimpleName() + ": Rendered by " + getClass().getSimpleName() + " with priority " + getPluginPriority();
    }
}
