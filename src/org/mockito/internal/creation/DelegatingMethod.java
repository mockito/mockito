/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.Method;

import org.mockito.internal.invocation.MockitoMethod;

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
    
    @Override
    public int hashCode() {
        return 1;
    }
    
    @Override
    public boolean equals(Object obj) {
        return method.equals(obj);
    }
}