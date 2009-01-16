/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.AnnotationEngine;

/**
 * Initializes fields annotated with &#064;Mock
 * <p>
 * See {@link MockitoAnnotations}
 */
public class DefaultAnnotationEngine implements AnnotationEngine {
    
    /* (non-Javadoc)
     * @see org.mockito.AnnotationEngine#createMockFor(java.lang.annotation.Annotation, java.lang.reflect.Field)
     */
    @SuppressWarnings("deprecation")
    public Object createMockFor(Annotation annotation, Field field) {
        if (annotation instanceof Mock || annotation instanceof org.mockito.MockitoAnnotations.Mock) {
            return Mockito.mock(field.getType(), field.getName());
        } else {
            return null;
        }
    }
}