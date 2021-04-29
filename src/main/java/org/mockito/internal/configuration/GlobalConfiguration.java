/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.io.Serializable;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.stubbing.Answer;

/**
 * Thread-safe wrapper on user-defined org.mockito.configuration.MockitoConfiguration implementation
 */
public class GlobalConfiguration implements IMockitoConfiguration, Serializable {
    private static final long serialVersionUID = -2860353062105505938L;

    private static final ThreadLocal<IMockitoConfiguration> GLOBAL_CONFIGURATION =
            new ThreadLocal<>();

    // back door for testing
    IMockitoConfiguration getIt() {
        return GLOBAL_CONFIGURATION.get();
    }

    public GlobalConfiguration() {
        // Configuration should be loaded only once but I cannot really test it
        if (GLOBAL_CONFIGURATION.get() == null) {
            GLOBAL_CONFIGURATION.set(createConfig());
        }
    }

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

    public AnnotationEngine getAnnotationEngine() {
        return GLOBAL_CONFIGURATION.get().getAnnotationEngine();
    }

    public org.mockito.plugins.AnnotationEngine tryGetPluginAnnotationEngine() {
        IMockitoConfiguration configuration = GLOBAL_CONFIGURATION.get();
        if (configuration.getClass() == DefaultMockitoConfiguration.class) {
            return Plugins.getAnnotationEngine();
        }
        return configuration.getAnnotationEngine();
    }

    @Override
    public boolean cleansStackTrace() {
        return GLOBAL_CONFIGURATION.get().cleansStackTrace();
    }

    @Override
    public boolean enableClassCache() {
        return GLOBAL_CONFIGURATION.get().enableClassCache();
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        return GLOBAL_CONFIGURATION.get().getDefaultAnswer();
    }
}
