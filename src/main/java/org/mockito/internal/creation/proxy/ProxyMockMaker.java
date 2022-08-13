/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.proxy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.LocationFactory;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.util.Platform;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.internal.invocation.DefaultInvocationFactory.createInvocation;
import static org.mockito.internal.util.StringUtil.join;

/**
 * A mock maker that is using the {@link Proxy} utility and is therefore only capable of mocking interfaces but
 * does not rely on manual byte code generation but only uses official and public Java API.
 */
public class ProxyMockMaker implements MockMaker {

    private static final Object[] EMPTY = new Object[0];

    private final ProxyRealMethod proxyRealMethod = ProxyRealMethod.make();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        boolean object = settings.getTypeToMock() == Object.class;
        Class<?>[] ifaces = new Class<?>[settings.getExtraInterfaces().size() + (object ? 0 : 1)];
        int index = 0;
        if (!object) {
            ifaces[index++] = settings.getTypeToMock();
        }
        ClassLoader classLoader = settings.getTypeToMock().getClassLoader();
        for (Class<?> iface : settings.getExtraInterfaces()) {
            ifaces[index++] = iface;
            classLoader = resolveCommonClassLoader(classLoader, iface);
        }
        return (T)
                Proxy.newProxyInstance(
                        resolveCommonClassLoader(classLoader, ProxyMockMaker.class),
                        ifaces,
                        new MockInvocationHandler(handler, settings));
    }

    private static ClassLoader resolveCommonClassLoader(ClassLoader mostSpecific, Class<?> type) {
        if (mostSpecific == null) {
            return type.getClassLoader();
        }
        ClassLoader candidate = type.getClassLoader();
        if (candidate == null || mostSpecific == candidate) {
            return mostSpecific;
        }
        while (candidate != null) {
            if (candidate == mostSpecific) {
                return type.getClassLoader();
            }
            candidate = candidate.getParent();
        }
        candidate = mostSpecific;
        while (candidate != null) {
            if (candidate == type.getClassLoader()) {
                return mostSpecific;
            }
            candidate = candidate.getParent();
        }
        return new CommonClassLoader(mostSpecific, type.getClassLoader());
    }

    @Override
    public MockHandler getHandler(Object mock) {
        if (!Proxy.isProxyClass(mock.getClass())) {
            return null;
        }
        InvocationHandler handler = Proxy.getInvocationHandler(mock);
        if (!(handler instanceof MockInvocationHandler)) {
            return null;
        }
        return ((MockInvocationHandler) handler).handler.get();
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MockInvocationHandler) Proxy.getInvocationHandler(mock)).handler.set(newHandler);
    }

    @Override
    public TypeMockability isTypeMockable(Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return type.isInterface() || type == Object.class;
            }

            @Override
            public String nonMockableReason() {
                return mockable() ? "" : "non-interface";
            }
        };
    }

    private class MockInvocationHandler implements InvocationHandler {

        private final AtomicReference<MockHandler<?>> handler;

        private final MockCreationSettings<?> settings;

        private MockInvocationHandler(MockHandler<?> handler, MockCreationSettings<?> settings) {
            this.handler = new AtomicReference<>(handler);
            this.settings = settings;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args == null) {
                args = EMPTY;
            }
            if (method.getDeclaringClass() == Object.class) {
                switch (method.getName()) {
                    case "hashCode":
                        return System.identityHashCode(proxy);
                    case "equals":
                        return proxy == args[0];
                    case "toString":
                        break;
                    default:
                        throw new MockitoException(
                                join(
                                        "Unexpected overridable method of Object class found",
                                        "",
                                        "The method "
                                                + method
                                                + " was not expected to be declared. Either your JVM build offers "
                                                + "non-official API or the current functionality is not supported",
                                        Platform.describe()));
                }
            }
            RealMethod realMethod;
            if (Modifier.isAbstract(method.getModifiers())) {
                realMethod = RealMethod.IsIllegal.INSTANCE;
            } else {
                realMethod = proxyRealMethod.resolve(proxy, method, args);
            }
            return handler.get()
                    .handle(
                            createInvocation(
                                    proxy,
                                    method,
                                    args,
                                    realMethod,
                                    settings,
                                    LocationFactory.create()));
        }
    }

    private static class CommonClassLoader extends ClassLoader {

        private final ClassLoader left, right;

        private CommonClassLoader(ClassLoader left, ClassLoader right) {
            super(null);
            this.left = left;
            this.right = right;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            try {
                return left.loadClass(name);
            } catch (ClassNotFoundException ignored) {
                return right.loadClass(name);
            }
        }
    }
}
