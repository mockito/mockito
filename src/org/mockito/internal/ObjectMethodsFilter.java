/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class ObjectMethodsFilter<T extends MockAwareInvocationHandler> implements MockAwareInvocationHandler {
    private final Method equalsMethod;

    private final Method hashCodeMethod;

    private final Method toStringMethod;

    private final T delegate;

    @SuppressWarnings("unchecked")
    public ObjectMethodsFilter(Class toMock, T delegate) {
        try {
            if (toMock.isInterface()) {
                toMock = Object.class;
            }
            equalsMethod = toMock.getMethod("equals", new Class[] { Object.class });
            hashCodeMethod = toMock.getMethod("hashCode", (Class[]) null);
            toStringMethod = toMock.getMethod("toString", (Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("An Object method could not be found!");
        }
        this.delegate = delegate;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
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

    private String mockToString(Object mock) {
        return "Mock for " + simpleName(mock);
    }

    public static String simpleName(Object mock) {
		if (mock.getClass().getInterfaces().length == 2) {
			return mock.getClass().getInterfaces()[0].getSimpleName();
		} else {
			return mock.getClass().getSuperclass().getSimpleName();
		}
	}

    public T getDelegate() {
        return delegate;
    }

    public void setMock(Object mock) {
        delegate.setMock(mock);
    }
}