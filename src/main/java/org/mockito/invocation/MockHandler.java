/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import java.io.Serializable;

/**
 * Mockito handler of an invocation on a mock. This is a core part of the API, the heart of Mockito.
 * See also the {@link org.mockito.plugins.MockMaker}.
 * <p>
 * This api is work in progress. Do not provide your own implementations.
 * Mockito will provide you with the implementation via other {@link org.mockito.plugins.MockMaker} methods.
 */
public interface MockHandler extends Serializable {
    /**
     * Takes an invocation object and handles it.
     * <p>
     * The default implementation provided by Mockito handles invocations by recording
     * method calls on mocks for further verification, captures the stubbing information when mock is stubbed,
     * returns the stubbed values for invocations that have been stubbed, and much more.
     *
     * @param invocation The invocation to handle
     * @return Result
     * @throws Throwable Throwable
     */
    Object handle(Invocation invocation) throws Throwable;
}
