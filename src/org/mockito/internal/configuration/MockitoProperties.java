/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.configuration.ReturnValues;

/**
 * Configuration properties 
 */
public class MockitoProperties {

    /**
     * Mockito uses it by default to get return values for unstubbed invocations 
     */
    public static final ReturnValues DEFAULT_RETURN_VALUES =  new DefaultReturnValues();
   
}