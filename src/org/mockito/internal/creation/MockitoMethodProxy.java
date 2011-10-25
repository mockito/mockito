/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.cglib.proxy.MethodProxy;

public interface MockitoMethodProxy {

    Object invokeSuper(Object target, Object[] arguments) throws Throwable;

    MethodProxy getMethodProxy();

}