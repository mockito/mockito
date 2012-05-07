/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations.Mock;

import java.lang.reflect.Field;

/**
 * Instantiates a mock on a field annotated by {@link Mock}
 */
@SuppressWarnings("deprecation")
public class MockitoAnnotationsMockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {

    public Object process(Mock annotation, Field field) {
        return Mockito.mock(field.getType(), field.getName());
    }
}
