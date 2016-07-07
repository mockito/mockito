/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.MockitoAnnotations;

/**
 * Configures mock creation logic behind &#064;Mock, &#064;Captor and &#064;Spy annotations
 * <p>
 * If you are interested then see implementations or source code of {@link MockitoAnnotations#initMocks(Object)}
 */
public interface AnnotationEngine {

    /**
     * Allows extending the interface to perform action on specific fields on the test class.
     * <p>
     * See the implementation of this method to figure out what is it for.
     * 
     * @param clazz Class where to extract field information, check implementation for details
     * @param testInstance Test instance
     */
    void process(Class<?> clazz, Object testInstance);
}