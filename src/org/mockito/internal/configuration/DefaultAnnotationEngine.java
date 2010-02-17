/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.*;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.base.MockitoException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * Initializes fields annotated with &#64;{@link org.mockito.Mock} or &#64;{@link org.mockito.Captor}.
 * <p/>
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
        }

        if (annotation instanceof Captor) {
            Class<?> type = field.getType();
            if (!ArgumentCaptor.class.isAssignableFrom(type)) {
                throw new MockitoException("@Captor field must be of the type ArgumentCaptor.\n" +
                		"Field: '" + field.getName() + "' has wrong type\n" +
                		//TODO add javadocs
                		"For info how to use @Captor annotations see examples in javadoc for MockitoAnnotations class.");
            }

            return ArgumentCaptor.forClass(Object.class);
        }

        if (annotation instanceof Spy) {
            throw new IllegalArgumentException("@Spy is not yet supported.");
        }

        return null;
    }
}