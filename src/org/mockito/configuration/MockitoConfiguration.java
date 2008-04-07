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
 * This class is thread-safe but every thread has own instance of configuration (ThreadLocal pattern).
 * <p>
 * Configuring mockito is a new concept that we evaluate. Please let us know if you find it useful. 
 */
public class MockitoConfiguration {
    
    private static final ThreadLocal<MockitoConfiguration> CONFIG = new ThreadLocal<MockitoConfiguration>();

    private ReturnValues returnValues;
    
    private MockitoConfiguration() {
        resetReturnValues();
    }
    
    /**
     * @return instance of a configuration 
     */
    public static MockitoConfiguration instance() {
        if (CONFIG.get() == null) {
            CONFIG.set(new MockitoConfiguration());
        }
        return CONFIG.get();
    }
    
    /**
     * @return current {@link ReturnValues} implementation
     */
    public ReturnValues getReturnValues() {
        return returnValues;
    }

    /**
     * Sets {@link ReturnValues} implementation. 
     * Allows to change the default (unstubbed) values returned by mocks. 
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