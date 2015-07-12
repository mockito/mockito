/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.configuration.MockitoConfiguration;

public class ConfigurationAccess {

    public static MockitoConfiguration getConfig() {
        return (MockitoConfiguration) new GlobalConfiguration().getIt();
    }
    
    public static IMockitoConfiguration getConfigAsInterface() {
        return new GlobalConfiguration().getIt();
    }
    
    public static void removeIt() {
        GlobalConfiguration.GLOBAL_CONFIGURATION.remove();
    }

}