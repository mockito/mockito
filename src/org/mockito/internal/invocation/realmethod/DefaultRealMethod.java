/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.realmethod;

import org.mockito.internal.creation.util.MockitoMethodProxy;

import java.io.Serializable;

public class DefaultRealMethod implements RealMethod, Serializable {

    private static final long serialVersionUID = -4596470901191501582L;
    private final MockitoMethodProxy methodProxy;

    public DefaultRealMethod(MockitoMethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }

    public Object invoke(Object target, Object[] arguments) throws Throwable {
        return methodProxy.invokeSuper(target, arguments);
    }
}
