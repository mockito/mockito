/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.util.ServiceLoader;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
import org.mockito.internal.IMockMaker;
import org.mockito.internal.creation.CglibMockMaker;

public class ClassPathLoader {
    
    /**
     * @return configuration loaded from classpath or null
     */
    @SuppressWarnings({"unchecked"})
    public IMockitoConfiguration loadConfiguration() {
        //Trying to get config from classpath
        Class configClass = null;
        try {
            configClass = (Class) Class.forName("org.mockito.configuration.MockitoConfiguration");
        } catch (ClassNotFoundException e) {
            //that's ok, it means there is no global config, using default one. 
            return null;
        }
        
        try {
            return (IMockitoConfiguration) configClass.newInstance();
        } catch (ClassCastException e) {
            throw new MockitoConfigurationException("\n" +
                    "MockitoConfiguration class must implement org.mockito.configuration.IMockitoConfiguration interface.", e);
        } catch (Exception e) {
            throw new MockitoConfigurationException("\n" +
                    "Unable to instantiate org.mockito.configuration.MockitoConfiguration class. Does it have a safe, no-arg constructor?", e);
        }
    }

    /**
     * Returns the best mock maker available for the current runtime. This scans
     * the classpath to find a mock maker plugin if one is available, allowing
     * mockito to run on alternative platforms like Android.
     */
    public static IMockMaker getMockMaker() {
        for (IMockMaker mockMaker : ServiceLoader.load(IMockMaker.class)) {
            return mockMaker; // return the first one service loader finds (if any)
        }
        return new CglibMockMaker(); // default implementation
    }
}