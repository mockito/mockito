/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.invocation.InvocationOnMock;

public abstract class BaseReturnValues implements ReturnValues {
    
    private DefaultReturnValues defaultReturnValues = new DefaultReturnValues();
    
    public Object valueFor(InvocationOnMock invocation) {
        Object returnByDefault = defaultReturnValues.valueFor(invocation);
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (returnByDefault != null || returnType == Void.TYPE) {
            return returnByDefault;
        }
        return returnValueFor(invocation);
    }

    protected abstract Object returnValueFor(InvocationOnMock invocation);
}