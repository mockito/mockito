/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.invocation.InvocationOnMock;

/**
 * Configures return values for an unstubbed invocation
 * <p>
 * Can be used in {@link Mockito#mock(Class, ReturnValues)}
 */
public interface ReturnValues {

    /**
     * return value for an unstubbed invocation
     * 
     * @param invocation placeholder for mock and a method
     * @return the return value
     */
    Object valueFor(InvocationOnMock invocation);
}