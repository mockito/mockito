/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter.resolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ArgumentCaptorParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(
        final ParameterContext parameterContext,
        final ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        return ArgumentCaptor.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(
        final ParameterContext parameterContext,
        final ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        final Class<?> target = getGenericType(parameter);
        return ArgumentCaptor.forClass(target);
    }

    private static Class<?> getGenericType(final Parameter parameter) {
        final Type type = parameter.getParameterizedType();
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Type actual = parameterizedType.getActualTypeArguments()[0];
            if (actual instanceof Class) {
                return (Class<?>) actual;
            } else if (actual instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) actual).getRawType();
            }
        }
        return Object.class;
    }

}
