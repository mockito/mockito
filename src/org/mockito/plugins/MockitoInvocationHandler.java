/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.mockito.internal.Incubating;
import org.mockito.invocation.Invocation;

import java.io.Serializable;

/**
 * Mockito handler of an invocation on a mock.
 *
 * <p>Takes an invocation object and handles it.
 * The Invocation instance should be created by the mock maker internal filter.</p>
 */
// TODO XXX Invocation is concrete and in another package, could we use a Factory and use an interface instead.
// see MethodInterceptorFilter
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
