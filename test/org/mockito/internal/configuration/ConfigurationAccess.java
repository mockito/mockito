package org.mockito.internal.configuration;

import org.mockito.configuration.MockitoConfiguration;

public class ConfigurationAccess {

    public static MockitoConfiguration getConfig() {
        return (MockitoConfiguration) new GlobalConfiguration().getIt();
    }
}