/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Optional;
import java.util.function.BiFunction;

public class GenericTypeMatch {

    private final MatchType matchType;
    private final VariableResolver resolver;

    private GenericTypeMatch(MatchType matchType, VariableResolver resolver) {
        this.matchType = matchType;
        this.resolver = resolver;
    }

    public boolean matches(GenericTypeMatch other) {
        return matchType.matches(other.matchType);
    }

    public static GenericTypeMatch ofField(Field field) {
        VariableResolver resolver = VariableResolver.ofField(field);
        MatchType matchType = MatchType.ofClassAndResolver(field.getType(), resolver);
        return new GenericTypeMatch(matchType, resolver);
    }

    public static GenericTypeMatch ofGenericAndRawType(Type genericType, Class<?> rawType) {
        VariableResolver resolver = genericType instanceof ParameterizedType
            ? VariableResolver.ofParameterizedAndRawType((ParameterizedType) genericType, rawType, null)
            : VariableResolver.empty();
        MatchType matchType = MatchType.ofClassAndResolver(rawType, resolver);
        return new GenericTypeMatch(matchType, resolver);
    }

    public Optional<GenericTypeMatch> findDeclaredField(Field field) {
        if (this.matchType instanceof HasClass) {
            Class<?> clazz = ((HasClass) this.matchType).getTheClass();
            Class<?> declaringClass = field.getDeclaringClass();
            if (declaringClass.equals(clazz)) {
                return createFieldTypeMatch(field);
            }
            else {
                return findAncestor(clazz, declaringClass).flatMap(typeMatch -> typeMatch.createFieldTypeMatch(field));
            }
        }
        return Optional.empty();
    }

    private Optional<GenericTypeMatch> createFieldTypeMatch(Field field) {
        if (field.getGenericType() instanceof TypeVariable && this.resolver != null) {
            Optional<Type> resolved = this.resolver.resolve((TypeVariable<?>) field.getGenericType());
            if (resolved.isPresent() && resolved.get() instanceof MatchType) {
                return createFieldTypeMatchForVariable((MatchType) resolved.get());
            }
        }
        VariableResolver resolver = VariableResolver.ofFieldAndResolver(field, this.resolver);
        MatchType matchType = MatchType.ofClassAndResolver(field.getType(), resolver);
        return Optional.of(new GenericTypeMatch(matchType, resolver));
    }

    private static Optional<GenericTypeMatch> createFieldTypeMatchForVariable(MatchType matchType) {
        VariableResolver resolver = matchType instanceof MatchParameterizedClass
            ? ((MatchParameterizedClass) matchType).toResolver()
            : VariableResolver.empty();
        return Optional.of(new GenericTypeMatch(matchType, resolver));
    }

    private Optional<GenericTypeMatch> findAncestor(Class<?> derived, Class<?> declaringClass) {
        Optional<GenericTypeMatch> optionalTypeMatch = forEachAncestor(derived, (genericType, rawType) ->
            declaringClass.equals(rawType)
                ? createAncestorTypeMatch(genericType, rawType)
                : Optional.empty());
        if (optionalTypeMatch.isEmpty()) {
            optionalTypeMatch = forEachAncestor(derived, (genericType, rawType) -> createAncestorTypeMatch(genericType, rawType)
                .flatMap(typeMatch -> typeMatch.findAncestor(rawType, declaringClass)));
        }
        return optionalTypeMatch;
    }

    private <T> Optional<T> forEachAncestor(Class<?> derived, BiFunction<Type, Class<?>, Optional<T>> mapper) {
        Class<?> superClass = derived.getSuperclass();
        if (superClass != null) {
            Optional<T> result = mapper.apply(derived.getGenericSuperclass(), superClass);
            if (result.isPresent()) {
                return result;
            }
        }
        Class<?>[] rawInterfaces = derived.getInterfaces();
        Type[] genericInterfaces = derived.getGenericInterfaces();
        for (int i = 0; i < rawInterfaces.length; i++) {
            Optional<T> result = mapper.apply(genericInterfaces[i], rawInterfaces[i]);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    Optional<GenericTypeMatch> createAncestorTypeMatch(Type genericAncestorType, Class<?> rawAncectorClass) {
        VariableResolver resolver;
        if (genericAncestorType instanceof ParameterizedType) {
            resolver = VariableResolver.ofParameterizedAndRawType((ParameterizedType) genericAncestorType, rawAncectorClass, this.resolver);
        }
        else {
            resolver = VariableResolver.empty();
        }
        MatchType matchType = MatchType.ofClassAndResolver(rawAncectorClass, resolver);
        return Optional.of(new GenericTypeMatch(matchType, resolver));
    }
}
