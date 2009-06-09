/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.realmethod;

import org.mockito.cglib.proxy.MethodProxy;


public class CGLIBProxyRealMethod implements RealMethod {

    private final MethodProxy methodProxy;

    public CGLIBProxyRealMethod(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }

    public Object invoke(Object target, Object[] arguments) throws Throwable {
        return methodProxy.invokeSuper(target, arguments);
    }
}
