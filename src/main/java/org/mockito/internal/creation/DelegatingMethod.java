/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethod implements MockitoMethod {

    private final Method method;

    public DelegatingMethod(Method method) {
        assert method != null : "Method cannot be null";
        this.method = method;
    }

    public Class<?>[] getExceptionTypes() {
        return method.getExceptionTypes();
    }

    public Method getJavaMethod() {
        return method;
    }

    public String getName() {
        return method.getName();
    }

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    public boolean isAbstract() {
        return (method.getModifiers() & Modifier.ABSTRACT) != 0;
    }

    /**
     * @return True if the input object is a DelegatingMethod which has an internal Method which is equal to the internal Method of this DelegatingMethod,
     * or if the input object is a Method which is equal to the internal Method of this DelegatingMethod.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof DelegatingMethod) {
            DelegatingMethod that = (DelegatingMethod) o;
            return method.equals(that.method);
        } else {
            return method.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}