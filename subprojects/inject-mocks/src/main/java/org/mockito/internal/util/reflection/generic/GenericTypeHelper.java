/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.*;
import java.util.Optional;

public final class GenericTypeHelper {

    private GenericTypeHelper() {}

    public static Class<?> getRawTypeOfType(Type type, VariableResolver resolver) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof TypeVariable && resolver != null) {
            return getRawTypeOfVariable((TypeVariable<?>) type, resolver);
        } else if (type instanceof GenericArrayType) {
            return getRawTypeOfComponentType((GenericArrayType) type, resolver);
        } else if (type instanceof WildcardType) {
            return getRawTypeOfWildcard((WildcardType) type, resolver);
        } else if (type instanceof HasClass) {
            return ((HasClass) type).getTheClass();
        } else {
            return Object.class;
        }
    }

    private static Class<?> getRawTypeOfVariable(
            TypeVariable<?> typeVariable, VariableResolver resolver) {
        Optional<Type> optionalResolved = resolver.resolve(typeVariable);
        if (optionalResolved.isPresent()) {
            Type resolvedType = optionalResolved.get();
            Class<?> rawType = getRawTypeOfType(resolvedType, resolver);
            if (Object.class.equals(rawType)) {
                Type[] upperBounds = typeVariable.getBounds();
                if (upperBounds.length > 0) {
                    return getRawTypeOfType(upperBounds[0], resolver);
                }
            }
            return rawType;
        } else {
            return Object.class;
        }
    }

    private static Class<?> getRawTypeOfComponentType(
            GenericArrayType genericArrayType, VariableResolver resolver) {
        Class<?> rawType = getRawTypeOfType(genericArrayType.getGenericComponentType(), resolver);
        return Array.newInstance(rawType, 0).getClass();
    }

    private static Class<?> getRawTypeOfWildcard(WildcardType type, VariableResolver resolver) {
        Type[] upperBounds = type.getUpperBounds();
        if (upperBounds.length > 0) {
            return getRawTypeOfType(upperBounds[0], resolver);
        } else {
            return Object.class;
        }
    }

    public static Optional<Type> remapType(Type type, VariableResolver remapResolver) {
        Type replacement = null;
        if (type instanceof WildcardType) {
            replacement = MatchWildcard.ofWildcardType((WildcardType) type);
        } else if (type instanceof TypeVariable && remapResolver != null) {
            Optional<Type> optReplacement = remapResolver.resolve((TypeVariable<?>) type);
            if (optReplacement.isPresent()) {
                replacement = optReplacement.get();
            }
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (remapResolver != null
                    && !remapResolver.isEmpty()
                    && parameterizedType.getRawType() instanceof Class) {
                replacement =
                        MatchParameterizedClass.ofClassAndResolver(
                                (Class<?>) parameterizedType.getRawType(), remapResolver);
            } else {
                Optional<MatchType> optMatchType =
                        MatchParameterizedClass.ofParameterizedType(parameterizedType, null);
                if (optMatchType.isPresent()) {
                    replacement = optMatchType.get();
                }
            }
        } else if (type instanceof GenericArrayType) {
            replacement = GenericTypeHelper.getRawTypeOfType(type, remapResolver);
        }
        return Optional.ofNullable(replacement);
    }
}
