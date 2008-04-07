/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.invocation.InvocationOnMock;

/**
 * Configures return values for an unstubbed invocation
 */
public interface ReturnValues {

    /**
     * @param invocation
     * @return default return value if no stubbed value found
     */
    Object valueFor(InvocationOnMock invocation);
}