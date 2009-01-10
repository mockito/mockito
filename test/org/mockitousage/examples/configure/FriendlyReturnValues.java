/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure;

import org.mockito.Mockito;
import org.mockito.ReturnValues;
import org.mockito.configuration.experimental.ConfigurationSupport;
import org.mockito.invocation.InvocationOnMock;

/**
 * tries to return mocks instead of nulls
 */
@SuppressWarnings("deprecation")
public class FriendlyReturnValues implements ReturnValues {

    @SuppressWarnings("deprecation")
    public Object valueFor(InvocationOnMock invocation) {

        Class<?> returnType = invocation.getMethod().getReturnType();

        Object defaultReturnValue = ConfigurationSupport.defaultValueFor(invocation);

        if (defaultReturnValue != null || !ConfigurationSupport.isMockable(returnType)) {
            return defaultReturnValue;
        } else {
            return Mockito.mock(returnType);
        }
    }
}