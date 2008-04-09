/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.exceptions.base.MockitoException;

/**
 * Allows configuring Mockito to enable custom 'mocking style'. 
 * It can be useful when working with legacy code, etc.
 * <p>
 * See examples from mockito/test/org/mockitousage/examples/configure subpackage. 
 * You may want to check out the project from svn repository to easily browse Mockito's test code.
 * <p>
 * Basic example:
 * <pre>
 *   //create an implementation of ReturnValues interface
 *   
 *   public class FriendlyReturnValues implements ReturnValues {
 *
 *   public Object valueFor(InvocationOnMock invocation) {
 *       
 *       Class<?> returnType = invocation.getMethod().getReturnType();
 *       
 *       Object defaultReturnValue = ConfigurationSupport.defaultValueFor(invocation);
 *       
 *       if (defaultReturnValue != null || !ConfigurationSupport.isMockable(returnType)) {
 *           return defaultReturnValue;
 *       } else { 
 *           return Mockito.mock(returnType);
 *       }
 *   }
 *   
 *   //finally, change the configuration: 
 *   
 *   MockitoConfiguration.instance().setReturnValues(new FriendlyReturnValues());
 * </pre>
 * <p>
 * Configuring Mockito is a new concept that we evaluate. Please let us know if you find it useful/harmful. 
 */
public class MockitoConfiguration {
    
    private static final ThreadLocal<MockitoConfiguration> CONFIG = new ThreadLocal<MockitoConfiguration>();

    private ReturnValues returnValues;
    
    private MockitoConfiguration() {
        resetReturnValues();
    }
    
    /**
     * gets the singleton instance of a configuration
     * 
     * @return configuration 
     */
    public static MockitoConfiguration instance() {
        if (CONFIG.get() == null) {
            CONFIG.set(new MockitoConfiguration());
        }
        return CONFIG.get();
    }
    
    /**
     * returns currently set {@link ReturnValues}
     * 
     * @return
     */
    public ReturnValues getReturnValues() {
        return returnValues;
    }

    /**
     * Sets {@link ReturnValues} implementation. 
     * <p>
     * Allows to change the values returned by unstubbed methods. 
     * 
     * @param returnValues
     */
    public void setReturnValues(ReturnValues returnValues) {
        if (returnValues == null) {
            throw new MockitoException("Cannot set null ReturnValues!");
        }
        this.returnValues = returnValues;
    }

    /**
     * Resets {@link ReturnValues} implementation to the default one: {@link MockitoProperties#DEFAULT_RETURN_VALUES}
     */
    public void resetReturnValues() {
        returnValues = MockitoProperties.DEFAULT_RETURN_VALUES;
    }
}