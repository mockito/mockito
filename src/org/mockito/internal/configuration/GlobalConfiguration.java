package org.mockito.internal.configuration;

import org.mockito.configuration.IMockitoConfiguration;

public class GlobalConfiguration {
    
    private static IMockitoConfiguration globalConfiguration;
    private static boolean loaded = false;

    static {
        //TODO how bad is it? What happens if some exception is thrown?
        if (!loaded) {
            globalConfiguration = readFromClasspath();
            loaded = true;
        }
    }
    
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
            throw new RuntimeException("MockitoConfiguration class should implement org.mockito.configuration.IMockitoConfiguration interface.");
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantianate MockitoConfiguration class. Does it have a public, no-arg constructor?", e);
        }
    }

    public static IMockitoConfiguration getConfig() {
        return globalConfiguration;
    }
}