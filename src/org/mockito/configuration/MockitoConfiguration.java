/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;


public class MockitoConfiguration {
    
    private static final ReturnValues DEFAULT_RETURN_VALUES = new DefaultReturnValues();
    private static final ThreadLocal<ReturnValues> CUSTOM_RETURN_VALUES = new ThreadLocal<ReturnValues>();
    
    public static ReturnValues defaultReturnValues() {
        return getCustomReturnValues() != null ? getCustomReturnValues() : DEFAULT_RETURN_VALUES;
    }

    public static void setCustomReturnValues(ReturnValues returnValues) {
        CUSTOM_RETURN_VALUES.set(returnValues);
    }
    
    public static ReturnValues getCustomReturnValues() {
        return CUSTOM_RETURN_VALUES.get();
    }

    public static void resetCustomReturnValues() {
        CUSTOM_RETURN_VALUES.remove();
    }
}