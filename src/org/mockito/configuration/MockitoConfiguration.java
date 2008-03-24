/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;


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
        this.returnValues = returnValues;
    }

    public void resetReturnValues() {
        returnValues = new DefaultReturnValues();
    }
}