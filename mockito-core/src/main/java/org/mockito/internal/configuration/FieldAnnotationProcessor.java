/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Simple annotation processor interface.
 */
public interface FieldAnnotationProcessor<A extends Annotation> {
    Object process(A annotation, Field field);
}
