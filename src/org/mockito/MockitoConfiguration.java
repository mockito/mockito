/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.stubbing.DefaultReturnValues;

public class MockitoConfiguration {
    
    private static ReturnValues returnValues = new DefaultReturnValues();
    
    public static ReturnValues defaultReturnValues() {
        return returnValues;
    }
}