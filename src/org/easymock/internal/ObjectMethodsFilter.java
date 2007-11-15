/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

import org.mockito.MockAwareInvocationHandler;

public class ObjectMethodsFilter<T extends MockAwareInvocationHandler> implements MockAwareInvocationHandler {
    private final Method equalsMethod;

    private final Method hashCodeMethod;

    private final Method toStringMethod;

    private final T delegate;

    private final String name;

    @SuppressWarnings("unchecked")
    public ObjectMethodsFilter(Class toMock, T delegate,
            String name) {
        if (name != null && !Invocation.isJavaIdentifier(name)) {
            throw new IllegalArgumentException(String.format("'%s' is not a valid Java identifier.", name));
            
        }
        try {
            if (toMock.isInterface()) {
                toMock = Object.class;
            }
            equalsMethod = toMock.getMethod("equals",
                    new Class[] { Object.class });
            hashCodeMethod = toMock.getMethod("hashCode", (Class[]) null);
            toStringMethod = toMock.getMethod("toString", (Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("An Object method could not be found!");
        }
        this.delegate = delegate;
        this.name = name;
    }

    public final Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (equalsMethod.equals(method)) {
            return Boolean.valueOf(proxy == args[0]);
        }
        if (hashCodeMethod.equals(method)) {
            return new Integer(System.identityHashCode(proxy));
        }
        if (toStringMethod.equals(method)) {
            return mockToString(proxy);
        }
        return delegate.invoke(proxy, method, args);
    }

    private String mockToString(Object proxy) {
        return (name != null) ? name : "EasyMock for " + mockType(proxy);
    }

    //TODO unit test it or check if tested properly
    private String mockType(Object proxy) {
		if (proxy.getClass().getInterfaces().length == 2) {
			return proxy.getClass().getInterfaces()[0].toString();
		} else {
			return proxy.getClass().getSuperclass().toString();
		}
	}

    public T getDelegate() {
        return delegate;
    }

    public void setMock(Object mock) {
        delegate.setMock(mock);
    }
}