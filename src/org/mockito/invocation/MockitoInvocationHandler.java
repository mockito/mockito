/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.internal.Incubating;
import org.mockito.invocation.Invocation;

import java.io.Serializable;

/**
 * Mockito handler of an invocation on a mock. This is a core part of the API, the heart of Mockito.
 * This type might be interesting for developers wishing to extend Mockito.
 * See also the {@link org.mockito.plugins.MockMaker}.
 * <p>
 * Takes an invocation object and handles it.
 * The Invocation instance should be created by the {@link org.mockito.plugins.MockMaker}.
 * <p>
 * The default implementation provided by Mockito handles invocations by recording
 * method calls on mocks for further verification, captures the stubbing information when mock is stubbed,
 * returns the stubbed values for invocations that have been stubbed, and much more.
 */
@Incubating
public interface MockitoInvocationHandler extends Serializable {

    /**
     * Handles the invocation.
     *
     * @param invocation The invocation to handle
     * @return Result
     * @throws Throwable Throwable
     */
    @Incubating
    Object handle(Invocation invocation) throws Throwable;

}
