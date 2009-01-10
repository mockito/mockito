package org.mockito.internal.configuration;

import org.mockito.IMockitoConfiguration;
import org.mockito.ReturnValues;

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
        IMockitoConfiguration defaultConfiguration = new IMockitoConfiguration() {
            public ReturnValues getReturnValues() {
                //For now, let's leave the deprecated way of getting return values, 
                //it will go away, replaced simply by return new DefaultReturnValues()
                return Configuration.instance().getReturnValues();
            }
        };
        IMockitoConfiguration config = new ClassPathLoader().loadConfiguration(defaultConfiguration);
        return config;
    }

    public ReturnValues getReturnValues() {
        return globalConfiguration.get().getReturnValues();
    }

    public static void validate() {
        new GlobalConfiguration();
    }
}