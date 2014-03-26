/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.ReturnValues;
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
@SuppressWarnings("deprecation")//suppressed until ReturnValues are removed
public class DefaultMockitoConfiguration implements IMockitoConfiguration {
    
    /* (non-Javadoc)
     * @see org.mockito.IMockitoConfiguration#getReturnValues()
     */
    @Deprecated
    public ReturnValues getReturnValues() {
        throw new RuntimeException("\n" + "This method should not be used by the framework because it was deprecated"
                + "\n" + "Please report the failure to the Mockito mailing list");
    }

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