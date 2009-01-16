/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.ReturnValues;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.exceptions.base.MockitoException;

/**
 * Singleton implementation of MockitoConfiguration
 */
@Deprecated
public class Configuration implements MockitoConfiguration {
    
    public static final ThreadLocal<Configuration> CONFIG = new ThreadLocal<Configuration>();

    private ReturnValues returnValues;
    
    private Configuration() {
        resetReturnValues();
    }
    
    /**
     * gets the singleton instance of a configuration
     */
    public static Configuration instance() {
        if (CONFIG.get() == null) {
            CONFIG.set(new Configuration());
        }
        return CONFIG.get();
    }
    
    /* (non-Javadoc)
     * @see org.mockito.internal.configuration.MockitoConfiguration#getReturnValues()
     */
    public ReturnValues getReturnValues() {
        return returnValues;
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.configuration.MockitoConfiguration#setReturnValues(org.mockito.configuration.ReturnValues)
     */
    public void setReturnValues(ReturnValues returnValues) {
        if (returnValues == null) {
            throw new MockitoException("Cannot set null ReturnValues!");
        }
        this.returnValues = returnValues;
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.configuration.MockitoConfiguration#resetReturnValues()
     */
    public void resetReturnValues() {
        //This is a bit messy but it's just to maintain this stupid and deprecated Configuration class
        //Once Configuration class is gone DefaultMockitoConfiguration will rule them all
        IMockitoConfiguration defaultConfiguration = new DefaultMockitoConfiguration();
        returnValues = defaultConfiguration.getReturnValues();
    }
}