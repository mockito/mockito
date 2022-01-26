/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.internal.invocation.AbstractAwareMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;

public class InvocationInfo implements AbstractAwareMethod {

    private final Method method;
    private final InvocationOnMock invocation;

    public InvocationInfo(InvocationOnMock theInvocation) {
        this.method = theInvocation.getMethod();
        this.invocation = theInvocation;
    }

    public boolean isValidException(final Throwable throwable) {
        if (isValidException(method, throwable)) {
            return true;
        }

        return isValidExceptionForParents(method.getDeclaringClass(), throwable);
    }

    private boolean isValidExceptionForParents(final Class<?> parent, final Throwable throwable) {
        final List<Class<?>> ancestors = new ArrayList<>(Arrays.asList(parent.getInterfaces()));

        if (parent.getSuperclass() != null) {
            ancestors.add(parent.getSuperclass());
        }

        final boolean validException =
                ancestors.stream()
                        .anyMatch(ancestor -> isValidExceptionForClass(ancestor, throwable));

        if (validException) {
            return true;
        }

        return ancestors.stream()
                .anyMatch(ancestor -> isValidExceptionForParents(ancestor, throwable));
    }

    private boolean isValidExceptionForClass(final Class<?> parent, final Throwable throwable) {
        try {
            final Method parentMethod =
                    parent.getMethod(this.method.getName(), this.method.getParameterTypes());
            return isValidException(parentMethod, throwable);
        } catch (NoSuchMethodException e) {
            // ignore interfaces that doesn't have such a method
            return false;
        }
    }

    private boolean isValidException(final Method method, final Throwable throwable) {
        final Class<?>[] exceptions = method.getExceptionTypes();
        final Class<?> throwableClass = throwable.getClass();
        for (final Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidReturnType(Class<?> clazz) {
        if (method.getReturnType().isPrimitive() || clazz.isPrimitive()) {
            return Primitives.primitiveTypeOf(clazz)
                    == Primitives.primitiveTypeOf(method.getReturnType());
        } else {
            return method.getReturnType().isAssignableFrom(clazz);
        }
    }

    /**
     * Returns {@code true} is the return type is {@link Void} or represents the pseudo-type to the keyword {@code void}.
     * E.g:  {@code void foo()} or {@code Void bar()}
     */
    public boolean isVoid() {
        final MockCreationSettings mockSettings =
                MockUtil.getMockHandler(invocation.getMock()).getMockSettings();
        Class<?> returnType =
                GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())
                        .resolveGenericReturnType(this.method)
                        .rawType();
        return returnType == Void.TYPE || returnType == Void.class;
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
