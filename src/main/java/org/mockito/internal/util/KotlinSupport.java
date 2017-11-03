/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.invocation.RealMethod;

/**
 * Provides the utilities for the Kotlin language.
 */
public final class KotlinSupport {

    private KotlinSupport() {
    }

    /**
     * Tests whether the given method has default implementation.
     *
     * @param method the method
     * @return {@code true} if it is a Kotlin interface function with default implementation, otherwise {@code false}
     */
    public static boolean hasDefaultImplementation(Method method) {
        return findDefaultImplMethod(method) != null;
    }

    /**
     * Returns the {@link RealMethod} representing default implementation of the given method. If the method is not a
     * Kotlin interface function with default implementation, {@code notFound} is returned.
     *
     * @param mock      the mock object
     * @param method    the method
     * @param arguments the arguments used for the given method call
     * @param notFound  the {@link RealMethod} to be returned if default implementation is not found.
     * @return the {@link RealMethod} representing default implementation, otherwise {@code notFound}
     */
    public static RealMethod getDefaultImplementation(Object mock,
                                                      Method method,
                                                      Object[] arguments,
                                                      RealMethod notFound) {
        final Method defaultImplMethod = findDefaultImplMethod(method);
        if (defaultImplMethod == null) {
            return notFound;
        }

        int length = arguments.length;
        final Object[] defaultImplMethodArguments = new Object[length + 1];
        defaultImplMethodArguments[0] = mock;
        System.arraycopy(arguments, 0, defaultImplMethodArguments, 1, length);

        return new RealMethod() {

            @Override
            public boolean isInvokable() {
                return true;
            }

            @Override
            public Object invoke() throws Throwable {
                try {
                    return defaultImplMethod.invoke(null, defaultImplMethodArguments);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    new ConditionalStackTraceFilter().filter(cause);
                    throw cause;
                }
            }

        };
    }

    private static Method findDefaultImplMethod(Method method) {
        if (!Modifier.isAbstract(method.getModifiers())) {
            return null;
        }

        Class<?> declaringClass = method.getDeclaringClass();
        Class<?> defaultImplsClass = findDefaultImplsClass(declaringClass);
        if (defaultImplsClass == null) {
            return null;
        }

        // The first argument type of default implementation is that interface itself. Others are parameter types of the
        // given method.
        Class<?>[] others = method.getParameterTypes();
        int length = others.length;
        Class<?>[] parameterTypes = new Class[length + 1];
        parameterTypes[0] = declaringClass;
        System.arraycopy(others, 0, parameterTypes, 1, length);

        try {
            // The method representing default implementation should be `public` and `static`.
            Method defaultImpl = defaultImplsClass.getMethod(method.getName(), parameterTypes);
            if (Modifier.isStatic(defaultImpl.getModifiers())) {
                return defaultImpl;
            }
        } catch (NoSuchMethodException ignored) {
        }

        return null;
    }

    private static Class<?> findDefaultImplsClass(Class<?> c) {
        if (c.isInterface() && isKotlinClass(c)) {
            for (Class<?> cls : c.getClasses()) {
                // The default implementation of the interface is in `DefaultImpls` class member of that interface.
                if (cls.getSimpleName().equals("DefaultImpls") && Modifier.isStatic(cls.getModifiers())) {
                    return cls;
                }
            }
        }
        return null;
    }

    private static boolean isKotlinClass(Class<?> c) {
        for (Annotation a : c.getDeclaredAnnotations()) {
            // All the Kotlin classes are marked with `kotlin.Metadata` annotation.
            if (a.annotationType().getName().equals("kotlin.Metadata")) {
                return true;
            }
        }
        return false;
    }

}
