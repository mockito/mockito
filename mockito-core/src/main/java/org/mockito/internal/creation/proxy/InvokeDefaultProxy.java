/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.proxy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.util.Platform;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.internal.util.StringUtil.join;

class InvokeDefaultProxy implements ProxyRealMethod {

    private final Method invokeDefault;

    InvokeDefaultProxy() throws Throwable {
        invokeDefault =
                InvocationHandler.class.getMethod(
                        "invokeDefault", Object.class, Method.class, Object[].class);
    }

    @Override
    public RealMethod resolve(Object proxy, Method method, Object[] args) {
        return new InvokeDefaultRealMethod(proxy, method, args);
    }

    private class InvokeDefaultRealMethod implements RealMethod, Serializable {

        private static final long serialVersionUID = -1;

        private final Object proxy;
        private final SerializableMethod serializableMethod;
        private final Object[] args;

        private InvokeDefaultRealMethod(Object proxy, Method method, Object[] args) {
            this.proxy = proxy;
            this.serializableMethod = new SerializableMethod(method);
            this.args = args;
        }

        @Override
        public boolean isInvokable() {
            return true;
        }

        @Override
        public Object invoke() throws Throwable {
            try {
                return invokeDefault.invoke(null, proxy, serializableMethod.getJavaMethod(), args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new MockitoException(
                        join(
                                "Failed to access default method or invoked method with illegal arguments",
                                "",
                                "Method "
                                        + serializableMethod.getJavaMethod()
                                        + " could not be delegated, this is not supposed to happen",
                                Platform.describe()),
                        e);
            }
        }
    }
}
