/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.configuration.experimental.ConfigurationSupport;
import org.mockito.invocation.InvocationOnMock;

/**
 * Configures return values for an unstubbed invocation
 * <p>
 * See examples in javadoc for {@link ConfigurationSupport}
 */
public interface ReturnValues {

    /**
     * returns value for an unstubbed invocation
     * <p>
     * See examples in javadoc for {@link ConfigurationSupport}
     * 
     * @param invocation placeholder for mock and a method
     * @return the return value
     */
    Object valueFor(InvocationOnMock invocation);
}