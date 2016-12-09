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
 *
 * <p>This interface can be used to configure a different annotation engine through
 * {@link org.mockito.configuration.IMockitoConfiguration}, however this mechanism is being superseded by the new
 * {@link org.mockito.plugins plugin} system.
 *
 * @deprecated Please use {@link org.mockito.plugins.AnnotationEngine} instead,
 *             this interface will probably be removed in mockito 3.
 */
@Deprecated
public interface AnnotationEngine extends org.mockito.plugins.AnnotationEngine {
}
