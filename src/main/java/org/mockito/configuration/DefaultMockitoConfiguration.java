/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.stubbing.Answer;

/**
 * DefaultConfiguration of Mockito framework
 * <p>
 * Currently it doesn't have many configuration options but it will probably change if future.
 * <p>
 * See javadocs for {@link IMockitoConfiguration} on info how to configure Mockito
 */
public class DefaultMockitoConfiguration implements IMockitoConfiguration {

    public Answer<Object> getDefaultAnswer() {
        return new ReturnsEmptyValues();
    }
    
    /* (non-Javadoc)
     * @see org.mockito.IMockitoConfiguration#getAnnotationEngine()
     */
    public AnnotationEngine getAnnotationEngine() {
        return new InjectingAnnotationEngine();
    }

    /* (non-Javadoc)
     * @see org.mockito.configuration.IMockitoConfiguration#cleansStackTrace()
     */
    public boolean cleansStackTrace() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.mockito.configuration.IMockitoConfiguration#enableClassCache()
     */
    public boolean enableClassCache() {
        return true;
    }
    
    
}