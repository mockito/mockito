/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

public class MockitoConfiguration {
    
    private static EmptyValuesProvider emptyValuesProvider = new EmptyValuesProvider();

    public static EmptyValuesProvider emptyValues() {
        return emptyValuesProvider;
    }
}