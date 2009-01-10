package org.mockito.internal.configuration;

import org.mockito.IMockitoConfiguration;
import org.mockito.exceptions.misusing.MockitoConfigurationException;

public class ClassPathLoader {
    
    /**
     * @param defaultConfiguration - to be used if no config found on classpath
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public IMockitoConfiguration loadConfiguration(IMockitoConfiguration defaultConfiguration) {
        //Trying to get config from classpath
        Class configClass = null;
        try {
            configClass = (Class) Class.forName("org.mockito.MockitoConfiguration");
        } catch (ClassNotFoundException e) {
            //that's ok, it means there is no global config, using default one. 
            return defaultConfiguration;
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
}