/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.internal.util.reflection.generic.GenericTypeHelper.remapType;

public interface VariableResolver {

    Optional<Type> resolve(TypeVariable<?> variable);

    default boolean isEmpty() {
        return false;
    }

    static VariableResolver ofFieldAndResolver(Field field, VariableResolver remapResolver) {
        return Factory.ofFieldAndResolver(field, remapResolver);
    }

    static VariableResolver ofParameterizedAndRawType(
            ParameterizedType parameterizedType, Class<?> rawType, VariableResolver remapResolver) {
        return Factory.ofParameterizedAndRawType(parameterizedType, rawType, remapResolver);
    }

    static VariableResolver empty() {
        return Factory.empty();
    }

    class Factory {

        public static final VariableResolver EMPTY_RESOLVER =
                new VariableResolver() {
                    @Override
                    public Optional<Type> resolve(TypeVariable<?> variable) {
                        return Optional.empty();
                    }

                    @Override
                    public boolean isEmpty() {
                        return true;
                    }
                };

        private static VariableResolver ofFieldAndResolver(
                Field field, VariableResolver remapResolver) {
            return ofGenericAndRawType(field.getGenericType(), field.getType(), remapResolver);
        }

        private static VariableResolver ofGenericAndRawType(
                Type genericType, Class<?> rawType, VariableResolver remapResolver) {
            if (genericType instanceof ParameterizedType) {
                return ofParameterizedAndRawType(
                        (ParameterizedType) genericType, rawType, remapResolver);
            } else if (genericType instanceof GenericArrayType && rawType.isArray()) {
                return ofGenericAndRawType(
                        ((GenericArrayType) genericType).getGenericComponentType(),
                        rawType.getComponentType(),
                        remapResolver);
            } else {
                return empty();
            }
        }

        private static VariableResolver ofParameterizedAndRawType(
                ParameterizedType parameterizedType,
                Class<?> rawType,
                VariableResolver remapResolver) {
            TypeVariable<?>[] parameters = rawType.getTypeParameters();
            Type[] arguments = parameterizedType.getActualTypeArguments();
            Map<TypeVariable<?>, Type> typeOfArguments = new HashMap<>();
            for (int i = 0; i < parameters.length; i++) {
                TypeVariable<?> parameter = parameters[i];
                Type argument = arguments[i];
                typeOfArguments.put(parameter, argument);
            }
            remap(typeOfArguments, remapResolver);
            return variable -> Optional.ofNullable(typeOfArguments.get(variable));
        }

        private static void remap(
                Map<TypeVariable<?>, Type> typeOfArguments, VariableResolver remapResolver) {
            for (Map.Entry<TypeVariable<?>, Type> entry : typeOfArguments.entrySet()) {
                TypeVariable<?> key = entry.getKey();
                Type value = entry.getValue();
                Optional<Type> remappedType = remapType(value, remapResolver);
                remappedType.ifPresent(type -> typeOfArguments.put(key, type));
            }
        }

        private static VariableResolver empty() {
            return EMPTY_RESOLVER;
        }
    }
}
