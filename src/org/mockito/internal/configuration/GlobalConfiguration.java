/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.ReturnValues;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.configuration.IMockitoConfiguration;

/**
 * Thread-safe wrapper on user-defined org.mockito.configuration.MockitoConfiguration implementation
 */
public class GlobalConfiguration implements IMockitoConfiguration {
    
    private static ThreadLocal<IMockitoConfiguration> globalConfiguration = new ThreadLocal<IMockitoConfiguration>();

    public GlobalConfiguration() {
        //Configuration should be loaded only once but I cannot really test it
        if (globalConfiguration.get() == null) {
            globalConfiguration.set(getConfig());
        }
    }
    
    @SuppressWarnings("deprecation")
    IMockitoConfiguration getConfig() {
        IMockitoConfiguration defaultConfiguration = new DefaultMockitoConfiguration() {
            @Override public ReturnValues getReturnValues() {
                //For now, let's leave the deprecated way of getting return values, 
                //it will go away, replaced simply by return new DefaultReturnValues()
                return Configuration.instance().getReturnValues();
            }
        };
        IMockitoConfiguration config = new ClassPathLoader().loadConfiguration();
        if (config != null) {
            return config;
        } else {
            return defaultConfiguration;
        }
    }

    public static void validate() {
        new GlobalConfiguration();
    }
    
    public ReturnValues getReturnValues() {
        return globalConfiguration.get().getReturnValues();
    }

    public AnnotationEngine getAnnotationEngine() {
        return globalConfiguration.get().getAnnotationEngine();
    }
}