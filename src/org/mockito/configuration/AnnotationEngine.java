/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.MockitoAnnotations;

/**
 * Configures mock creation logic behind &#064;Mock annotations
 * <p>
 * If you are interested then see implementations or source code of {@link MockitoAnnotations#initMocks(Object)}
 */
public interface AnnotationEngine {

    /**
     * Usually the implementation checks the annotation  
     * and then creates a mock object for specified field.
     * <p>
     * You don't need to set the mock on the field. Mockito does it for you. 
     * If in doubts look for implementations of this interface.
     * 
     * @param annotation annotation on the field, for example &#064;Mock
     * @param field field to create mock object for
     * @return mock created for specified field. Can be null - then Mockito will not initialize the field
     */
    Object createMockFor(Annotation annotation, Field field);

}