/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

/**
 * Configures test via annotations.
 *
 * <p>Mockito default engine handles the logic behind &#064;Mock, &#064;Captor, &#064;Spy and &#064;InjectMocks annotations.
 *
 * <p>This interface is an extension point that make possible to use a different annotation engine allowing to extend
 * or replace mockito default engine.
 *
 * <p>
 * If you are interested then see implementations or source code of {@link org.mockito.MockitoAnnotations#openMocks(Object)}
 *
 * <p>This plugin mechanism supersedes the {@link org.mockito.configuration.IMockitoConfiguration}
 * in regard of switching mockito components.
 */
public interface AnnotationEngine {
    /**
     * Processes the test instance to configure annotated members.
     *
     * @param clazz Class where to extract field information, check implementation for details
     * @param testInstance Test instance
     */
    AutoCloseable process(Class<?> clazz, Object testInstance);

    class NoAction implements AutoCloseable {

        @Override
        public void close() {}
    }
}
