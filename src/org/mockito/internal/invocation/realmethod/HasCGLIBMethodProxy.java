/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.realmethod;

import org.mockito.cglib.proxy.MethodProxy;

public interface HasCGLIBMethodProxy {

    MethodProxy getMethodProxy();
}
