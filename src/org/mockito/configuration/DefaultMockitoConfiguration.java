/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.ReturnValues;
import org.mockito.internal.configuration.DefaultAnnotationEngine;
import org.mockito.internal.returnvalues.EmptyReturnValues;

/**
 * DefaultConfiguration of Mockito framework
 * <p>
 * Currently it doesn't have many configuration options but it will probably change if future.
 * <p>
 * See javadocs for {@link IMockitoConfiguration} on info how to configure Mockito
 */
public class DefaultMockitoConfiguration implements IMockitoConfiguration {
    
    /* (non-Javadoc)
     * @see org.mockito.IMockitoConfiguration#getReturnValues()
     */
    public ReturnValues getReturnValues() {
        return new EmptyReturnValues();
    }
    
    /* (non-Javadoc)
     * @see org.mockito.IMockitoConfiguration#getAnnotationEngine()
     */
    public AnnotationEngine getAnnotationEngine() {
        return new DefaultAnnotationEngine();
    }
}