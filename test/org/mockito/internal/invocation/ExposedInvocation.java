/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.internal.creation.MockitoMethodProxy;
import org.mockito.internal.invocation.realmethod.HasCGLIBMethodProxy;

public class ExposedInvocation {

    private final MockitoMethodProxy methodProxy;

    public ExposedInvocation(InvocationImpl toBeExposed) {
         methodProxy = ((HasCGLIBMethodProxy) toBeExposed.realMethod).getMethodProxy();
    }

    public MockitoMethodProxy getMethodProxy() {
        return methodProxy;
    }
}