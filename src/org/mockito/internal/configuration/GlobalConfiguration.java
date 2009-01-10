package org.mockito.internal.configuration;

import org.mockito.IMockitoConfiguration;
import org.mockito.exceptions.misusing.MockitoConfigurationException;

public class GlobalConfiguration {
    
    private static IMockitoConfiguration globalConfiguration;
    private static boolean initialized = false;

    @SuppressWarnings("unchecked")
    private static IMockitoConfiguration readFromClasspath() {
        //Trying to get config from classpath
        Class configClass = null;
        try {
            configClass = (Class) Class.forName("org.mockito.MockitoConfiguration");
        } catch (ClassNotFoundException e) {
            //that's ok, it means there is no global config, 
            return null;
        }
        
        try {
            return (IMockitoConfiguration) configClass.newInstance();
        } catch (ClassCastException e) {
            throw new MockitoConfigurationException("\n" +
                    "MockitoConfiguration class must implement org.mockito.configuration.IMockitoConfiguration interface.", e);
        } catch (Exception e) {
            throw new MockitoConfigurationException("\n" +
                    "Unable to instantianate org.mockito.MockitoConfiguration class. Does it have a safe, no-arg constructor?", e);
        }
    }

    public static IMockitoConfiguration getConfig() {
        if (!initialized) {
            //TODO check email of mockito group
            throw new IllegalStateException("Something went wrong. GlobalConfiguration should be initialised by now.\n" +
                "Please report issue at http://mockito.org or write an email to mockito@googlegroups.com");
        }
        return globalConfiguration;
    }

    public static void init() {
        if (!initialized) {
            globalConfiguration = readFromClasspath();
            initialized = true;
        }
    }
}