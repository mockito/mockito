/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;

import java.io.Serializable;
import java.lang.reflect.Method;

public class ObjectMethodsGuru implements Serializable {

    public boolean isToString(Method method) {
        return isToString(new DelegatingMethod(method));
    }

    public boolean isToString(MockitoMethod method) {
        return method.getReturnType() == String.class
                && method.getParameterTypes().length == 0
                && method.getName().equals("toString");
    }

    public boolean isEqualsMethod(Method method) {
        return method.getName().equals("equals")
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Object.class;
    }

    public boolean isHashCodeMethod(Method method) {
        return method.getName().equals("hashCode")
                && method.getParameterTypes().length == 0;
    }

    public boolean isCompareToMethod(Method method) {
        return Comparable.class.isAssignableFrom(method.getDeclaringClass())
                && method.getName().equals("compareTo")
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == method.getDeclaringClass();
    }
}