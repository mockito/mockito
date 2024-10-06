/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.proxy;

import org.mockito.internal.SuppressSignatureCheck;
import org.mockito.internal.invocation.RealMethod;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@SuppressSignatureCheck
class MethodHandleProxy implements ProxyRealMethod {

    private final MethodHandles.Lookup lookup;

    MethodHandleProxy() throws Throwable {
        lookup = MethodHandles.lookup();
    }

    @Override
    public RealMethod resolve(Object proxy, Method method, Object[] args) {
        try {
            return new MethodHandleRealMethod(
                    lookup.findSpecial(
                                    method.getDeclaringClass(),
                                    method.getName(),
                                    MethodType.methodType(
                                            method.getReturnType(), method.getParameterTypes()),
                                    method.getDeclaringClass())
                            .bindTo(proxy),
                    args);
        } catch (Throwable ignored) {
            return RealMethod.IsIllegal.INSTANCE;
        }
    }

    @SuppressSignatureCheck
    static class LegacyVersion implements ProxyRealMethod {

        private final Constructor<MethodHandles.Lookup> constructor;

        LegacyVersion() throws Throwable {
            try {
                Class.forName("java.lang.Module");
                throw new RuntimeException("Must not be used when modules are available");
            } catch (ClassNotFoundException ignored) {
            }
            constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);
        }

        @Override
        public RealMethod resolve(Object proxy, Method method, Object[] args) {
            try {
                return new MethodHandleRealMethod(
                        constructor
                                .newInstance(method.getDeclaringClass())
                                .in(method.getDeclaringClass())
                                .unreflectSpecial(method, method.getDeclaringClass())
                                .bindTo(proxy),
                        args);
            } catch (Throwable ignored) {
                return RealMethod.IsIllegal.INSTANCE;
            }
        }
    }

    @SuppressSignatureCheck
    private static class MethodHandleRealMethod implements RealMethod, Serializable {

        private static final long serialVersionUID = -1;

        private final MethodHandle handle;
        private final Object[] args;

        private MethodHandleRealMethod(MethodHandle handle, Object[] args) {
            this.handle = handle;
            this.args = args;
        }

        @Override
        public boolean isInvokable() {
            return true;
        }

        @Override
        public Object invoke() throws Throwable {
            return handle.invokeWithArguments(args);
        }
    }
}
