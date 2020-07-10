/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.MockitoAnnotations;

/**
 * Configures mock creation logic behind &#064;Mock, &#064;Captor and &#064;Spy annotations
 * <p>
 * If you are interested then see implementations or source code of {@link MockitoAnnotations#openMocks(Object)}
 *
 * <p>This interface can be used to configure a different annotation engine through
 * {@link org.mockito.configuration.IMockitoConfiguration}, however this mechanism is being superseded by the new
 * {@link org.mockito.plugins plugin} system.
 *
 * <p>
 * Note that if it exists on the classpath both a class <code>org.mockito.configuration.MockitoConfiguration</code>
 * and a file <code>mockito-extensions/org.mockito.plugins.AnnotationEngine</code> then the implementation of
 * <code>org.mockito.configuration.MockitoConfiguration</code> will be chosen instead of the one in the file.
 *
 * @deprecated Please use {@link org.mockito.plugins.AnnotationEngine} instead,
 *             this interface will probably be removed in mockito 4.
 */
@Deprecated
public interface AnnotationEngine extends org.mockito.plugins.AnnotationEngine {}
