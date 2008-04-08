/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import java.lang.reflect.Modifier;

import org.mockito.invocation.InvocationOnMock;

/**
 * Handy implementation of {@link ReturnValues} that already deals with void types and provides some useful methods.
 * <p>
 * Intended for subclassing and providing the implementation for {@link BaseReturnValues#returnValueFor(InvocationOnMock)}.
 * <p>
 * See examples from mockito/test/org/mockitousage/examples/configure subpackage. 
 * You may want to check out the project from svn repository to easily browse Mockito's test code.
 */
public abstract class BaseReturnValues implements ReturnValues {
    
    /* (non-Javadoc)
     * @see org.mockito.configuration.ReturnValues#valueFor(org.mockito.invocation.InvocationOnMock)
     */
    public Object valueFor(InvocationOnMock invocation) {
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (isVoid(returnType)) {
            return null;
        }
        return returnValueFor(invocation);
    }

    /**
     * Override this method to return different value for given invocation.
     * 
     * @param invocation
     * @return return value for an invocation
     */
    protected abstract Object returnValueFor(InvocationOnMock invocation);
    
    /**
     * what Mockito returns by default for given invocation 
     * 
     * @param invocation
     * @return default return value
     */
    protected Object defaultValueFor(InvocationOnMock invocation) {
        return MockitoProperties.DEFAULT_RETURN_VALUES.valueFor(invocation);
    }

    /**
     * @param clazz
     * @return returns true if clazz is final
     */
    protected boolean isFinalClass(Class<?> clazz) {
        return Modifier.isFinal(clazz.getModifiers());
    }
    
    /**
     * @param clazz
     * @return true if clazz is void (primitive class)
     */    
    protected boolean isVoid(Class<?> returnType) {
        return returnType == Void.TYPE;
    }
}