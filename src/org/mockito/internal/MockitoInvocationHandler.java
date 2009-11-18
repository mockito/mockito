/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.io.Serializable;

import org.mockito.internal.invocation.Invocation;

public interface MockitoInvocationHandler extends Serializable {

    Object handle(Invocation invocation) throws Throwable;

}
