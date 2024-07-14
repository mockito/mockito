/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public interface MatchType extends Type {

    boolean matchesSource(MatchType other);

    static MatchType ofGenericAndRawTypeAndResolver(
            Type genericType, Class<?> clazz, VariableResolver resolver) {
        if (genericType instanceof ParameterizedType) {
            return MatchParameterizedClass.ofParameterizedType(
                            (ParameterizedType) genericType, resolver)
                    .get();
        }
        TypeVariable<? extends Class<?>>[] parameters = clazz.getTypeParameters();
        if (parameters.length > 0) {
            return MatchParameterizedClass.ofClassAndResolver(clazz, resolver);
        } else if (clazz.isArray() && genericType instanceof GenericArrayType) {
            return MatchArrayClass.ofClassAndResolver(
                    (GenericArrayType) genericType, clazz, resolver);
        } else {
            return MatchClass.ofClass(clazz);
        }
    }

    Type getOriginalType();
}
