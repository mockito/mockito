package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface VariableResolver {

    Optional<Type> resolve(TypeVariable<?> variable);

    default boolean isEmpty() {
        return false;
    }

    static VariableResolver ofField(Field field) {
        return Factory.ofField(field);
    }

    static VariableResolver ofFieldAndResolver(Field field, VariableResolver remapResolver) {
        return Factory.ofFieldAndResolver(field, remapResolver);
    }

    static VariableResolver ofParameterizedAndRawType(ParameterizedType parameterizedType, Class<?> rawType, VariableResolver remapResolver) {
        return Factory.ofParameterizedAndRawType(parameterizedType, rawType, remapResolver);
    }

    static VariableResolver empty() {
        return Factory.empty();
    }

    class Factory {

        public static final VariableResolver EMPTY_RESOLVER = new VariableResolver() {
            @Override
            public Optional<Type> resolve(TypeVariable<?> variable) {
                return Optional.empty();
            }

            @Override
            public boolean isEmpty() {
                return true;
            }
        };

        private static VariableResolver ofField(Field field) {
            return ofFieldAndResolver(field, null);
        }

        private static VariableResolver ofFieldAndResolver(Field field, VariableResolver remapResolver) {
            return ofGenericAndRawType(field.getGenericType(), field.getType(), remapResolver);
        }

        private static VariableResolver ofGenericAndRawType(Type genericType, Class<?> rawType, VariableResolver remapResolver) {
            if (genericType instanceof ParameterizedType) {
                return ofParameterizedAndRawType((ParameterizedType) genericType, rawType, remapResolver);
            }
            else if (genericType instanceof GenericArrayType && rawType.isArray()) {
                return ofGenericAndRawType(
                    ((GenericArrayType) genericType).getGenericComponentType(),
                    rawType.getComponentType(),
                    remapResolver);
            }
            else {
                return empty();
            }
        }

        private static VariableResolver ofParameterizedAndRawType(ParameterizedType parameterizedType,
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

        private static void remap(Map<TypeVariable<?>, Type> typeOfArguments, VariableResolver remapResolver) {
            for (Map.Entry<TypeVariable<?>, Type> entry : typeOfArguments.entrySet()) {
                TypeVariable<?> key = entry.getKey();
                Type value = entry.getValue();
                Type replacement = null;
                if (value instanceof WildcardType) {
                    replacement = MatchWildcard.ofWildcardType((WildcardType) value);
                }
                else if (value instanceof TypeVariable && remapResolver != null) {
                    Optional<Type> optReplacement = remapResolver.resolve((TypeVariable<?>) value);
                    if (optReplacement.isPresent()) {
                        replacement = optReplacement.get();
                        if (replacement instanceof MatchWildcard) {
                            replacement = ((MatchWildcard) replacement).makeCaptured();
                        }
                    }
                }
                else if (value instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) value;
                    if (remapResolver != null && !remapResolver.isEmpty() && parameterizedType.getRawType() instanceof Class) {
                        replacement = MatchParameterizedClass.ofClassAndResolver((Class<?>) parameterizedType.getRawType(), remapResolver);
                    }
                    else {
                        Optional<MatchType> optMatchType = MatchParameterizedClass.ofParameterizedType(parameterizedType);
                        if (optMatchType.isPresent()) {
                            replacement = optMatchType.get();
                        }
                    }
                }
                else if (value instanceof GenericArrayType) {
                    replacement = GenericTypeHelper.getRawTypeOfType(value, remapResolver);
                }
                if (replacement != null) {
                    typeOfArguments.put(key, replacement);
                }
            }
        }

        private static VariableResolver empty() {
            return EMPTY_RESOLVER;
        }
    }
}
