/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.exceptions.base.MockitoException;

public class MockitoConfiguration {
    
    private static final ThreadLocal<MockitoConfiguration> CONFIG = new ThreadLocal<MockitoConfiguration>();

    private ReturnValues returnValues;
    
    private MockitoConfiguration() {
        resetReturnValues();
    }
    
    public static MockitoConfiguration instance() {
        if (CONFIG.get() == null) {
            CONFIG.set(new MockitoConfiguration());
        }
        return CONFIG.get();
    }
    
    public ReturnValues getReturnValues() {
        return returnValues;
    }

    public void setReturnValues(ReturnValues returnValues) {
        if (returnValues == null) {
            throw new MockitoException("Cannot set null ReturnValues!");
        }
        this.returnValues = returnValues;
    }

    public void resetReturnValues() {
        returnValues = MockitoProperties.DEFAULT_RETURN_VALUES;
    }
}