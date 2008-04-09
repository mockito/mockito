/*
 * Copyright (c) 2007, Szczepan Faber. 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure;

import org.mockito.Mockito;
import org.mockito.configuration.ConfigurationSupport;
import org.mockito.configuration.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

/**
 * tries to return mocks instead of nulls
 */
public final class FriendlyReturnValues implements ReturnValues {

    public Object valueFor(InvocationOnMock invocation) {
        
        Class<?> returnType = invocation.getMethod().getReturnType();
        
        Object defaultReturnValue = ConfigurationSupport.defaultValueFor(invocation);
        
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        } else if (ConfigurationSupport.isMockable(returnType)) { 
            return Mockito.mock(returnType);
        } else {
            return defaultReturnValue;
        }
    }
}