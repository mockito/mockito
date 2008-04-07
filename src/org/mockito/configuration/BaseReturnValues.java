/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.invocation.InvocationOnMock;

/**
 * Handy implementation of {@link ReturnValues} that already deals with void types and default return values.
 * <p>
 * Intended for subclassing and providing implementation for {@link BaseReturnValues#returnValueFor(InvocationOnMock)}.
 * <p>
 * See examples from mockito/test/org/mockitousage/examples/configure subpackage. 
 * You may want to check out the project from svn repository to easily browse Mockito's test code.
 */
public abstract class BaseReturnValues implements ReturnValues {
    
    private DefaultReturnValues defaultReturnValues = new DefaultReturnValues();
    
    /* (non-Javadoc)
     * @see org.mockito.configuration.ReturnValues#valueFor(org.mockito.invocation.InvocationOnMock)
     */
    public Object valueFor(InvocationOnMock invocation) {
        Object returnByDefault = defaultReturnValues.valueFor(invocation);
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (returnByDefault != null || returnType == Void.TYPE) {
            return returnByDefault;
        }
        return returnValueFor(invocation);
    }

    /**
     * The default implementation already evaluated invocation and decided to return null.
     * Override this method to return different value than null. 
     * 
     * @param invocation
     * @return return value for an invocation
     */
    protected abstract Object returnValueFor(InvocationOnMock invocation);
}