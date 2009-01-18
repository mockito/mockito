/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.returnvalues;

import org.mockito.ReturnValues;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.InvocationOnMock;

/**
 * ReturnValues from global configuration
 */
public class GloballyConfiguredReturnValues implements ReturnValues {
    
    public Object valueFor(InvocationOnMock invocation) {
        return new GlobalConfiguration().getReturnValues().valueFor(invocation);
    }
}