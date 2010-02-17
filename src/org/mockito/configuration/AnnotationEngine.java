/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.MockitoAnnotations;

/**
 * Configures mock creation logic behind &#064;Mock, &#064;Captor and &#064;Spy annotations
 * <p>
 * If you are interested then see implementations or source code of {@link MockitoAnnotations#initMocks(Object)}
 */
public interface AnnotationEngine {

    /**
     * Creates mock, ArgumentCaptor or wraps field instance in spy object.
     * Only if of correct annotation type.
     *
     * @param annotation
     * @param field
     * @return
     */
    Object createMockFor(Annotation annotation, Field field);

}