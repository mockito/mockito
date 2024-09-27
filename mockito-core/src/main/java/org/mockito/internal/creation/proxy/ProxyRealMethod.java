/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.proxy;

import org.mockito.internal.invocation.RealMethod;

import java.lang.reflect.Method;

interface ProxyRealMethod {

    RealMethod resolve(Object proxy, Method method, Object[] args);

    static ProxyRealMethod make() {
        // From Java 16 on, there is a standard API for invoking a default method from an invocation
        // handler.
        try {
            return new InvokeDefaultProxy();
        } catch (Throwable ignored) {
        }
        // Java 8 does not yet allow special method invocation via proxies. Therefore, we need to
        // deep reflect what is no longer allowed after Java 8.
        try {
            return new MethodHandleProxy.LegacyVersion();
        } catch (Throwable ignored) {
        }
        // Between Java 9 and 15, a default method can be invoked via regular method handle
        // invocation.
        try {
            return new MethodHandleProxy();
        } catch (Throwable ignored) {
        }
        // Nothing works, this might happen on Android where method handles are not supported on old
        // versions. Default methods cannot be invoked.
        return (proxy, method, args) -> RealMethod.IsIllegal.INSTANCE;
    }
}
