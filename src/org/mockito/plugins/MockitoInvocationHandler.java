/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import java.io.Serializable;

import org.mockito.internal.Incubating;
import org.mockito.internal.invocation.Invocation;

/**
 * Handles the invocation on a mock
 *
 * TODO - should it extend serializable?
 */
@Incubating
public interface MockitoInvocationHandler extends Serializable {

    @Incubating
    Object handle(Invocation invocation) throws Throwable;

}
