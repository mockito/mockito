/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.invocation.realmethod.HasCGLIBMethodProxy;

public class ExposedInvocation {

    private final MethodProxy methodProxy;

    public ExposedInvocation(Invocation toBeExposed) {
         methodProxy = ((HasCGLIBMethodProxy) toBeExposed.realMethod).getMethodProxy();
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }
}