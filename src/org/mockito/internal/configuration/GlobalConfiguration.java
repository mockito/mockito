/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.ReturnValues;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.stubbing.Answer;

/**
 * Thread-safe wrapper on user-defined org.mockito.configuration.MockitoConfiguration implementation
 */
@SuppressWarnings("deprecation")//supressed until ReturnValues are removed
public class GlobalConfiguration implements IMockitoConfiguration {
    
    private static ThreadLocal<IMockitoConfiguration> globalConfiguration = new ThreadLocal<IMockitoConfiguration>();

    //back door for testing
    IMockitoConfiguration getIt() {
        return globalConfiguration.get();
    }
    
    public GlobalConfiguration() {
        //Configuration should be loaded only once but I cannot really test it
        if (globalConfiguration.get() == null) {
            globalConfiguration.set(createConfig());
        }
    }
    
    @SuppressWarnings("deprecation")
    private IMockitoConfiguration createConfig() {
        IMockitoConfiguration defaultConfiguration = new DefaultMockitoConfiguration();
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

    public boolean cleansStackTrace() {
        return globalConfiguration.get().cleansStackTrace();
    }

    public Answer<Object> getDefaultAnswer() {
        return globalConfiguration.get().getDefaultAnswer();
    }
}