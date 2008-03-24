/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.stubbing.DefaultReturnValues;

public class MockitoConfiguration {
    
    private static final ReturnValues DEFAULT_RETURN_VALUES = new DefaultReturnValues();
    private static final ThreadLocal<ReturnValues> CUSTOM_RETURN_VALUES = new ThreadLocal<ReturnValues>();
    
    public static ReturnValues defaultReturnValues() {
        return CUSTOM_RETURN_VALUES.get() != null ? CUSTOM_RETURN_VALUES.get() : DEFAULT_RETURN_VALUES;
    }

    public static void setCustomReturnValues(ReturnValues returnValues) {
        MockitoConfiguration.CUSTOM_RETURN_VALUES.set(returnValues);
    }

    public static void resetCustomReturnValues() {
        MockitoConfiguration.CUSTOM_RETURN_VALUES.remove();
    }
}