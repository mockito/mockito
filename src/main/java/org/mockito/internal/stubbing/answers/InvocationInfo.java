/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.mockito.internal.invocation.AbstractAwareMethod;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

public class InvocationInfo implements AbstractAwareMethod {

    private final Method method;

    public InvocationInfo(InvocationOnMock theInvocation) {
        this.method = theInvocation.getMethod();
    }

    public boolean isValidException(Throwable throwable) {
        Class<?>[] exceptions = method.getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidReturnType(Class<?> clazz) {
        if (method.getReturnType().isPrimitive() || clazz.isPrimitive()) {
            return Primitives.primitiveTypeOf(clazz) == Primitives.primitiveTypeOf(method.getReturnType());
        } else {
            return method.getReturnType().isAssignableFrom(clazz);
        }
    }

    /**
     * Returns {@code true} is the return type is {@link Void} or represents the pseudo-type to the keyword {@code void}.
     * E.g:  {@code void foo()} or {@code Void bar()}
     */
    public boolean isVoid() {
        Class<?> returnType = this.method.getReturnType();
        return returnType == Void.TYPE|| returnType == Void.class;
    }

    public String printMethodReturnType() {
        return method.getReturnType().getSimpleName();
    }

    public String getMethodName() {
        return method.getName();
    }

    public boolean returnsPrimitive() {
        return method.getReturnType().isPrimitive();
    }

    public Method getMethod() {
        return method;
    }

    public boolean isDeclaredOnInterface() {
        return method.getDeclaringClass().isInterface();
    }

    @Override
    public boolean isAbstract() {
        return (method.getModifiers() & Modifier.ABSTRACT) != 0;
    }
}
