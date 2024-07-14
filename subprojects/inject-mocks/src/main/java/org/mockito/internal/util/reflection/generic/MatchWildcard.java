/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MatchWildcard implements MatchType {

    protected final WildcardType wildcard;

    protected MatchWildcard(WildcardType wildcard) {
        this.wildcard = wildcard;
    }

    @Override
    public boolean matchesSource(MatchType other) {
        return sourceMatches(other);
    }

    @Override
    public Type getOriginalType() {
        return wildcard;
    }

    static MatchType ofWildcardType(WildcardType wildcardType) {
        return new MatchWildcard(wildcardType);
    }

    /**
     * @return true if this wildcard can be assigned a value of {@code sourceType}
     */
    public boolean sourceMatches(Type sourceType) {
        if (sourceType instanceof MatchType) {
            sourceType = ((MatchType) sourceType).getOriginalType();
        }
        if (sourceType instanceof WildcardType) {
            Type[] upperBoundsThis = this.wildcard.getUpperBounds();
            Type[] upperBoundsOther = ((WildcardType) sourceType).getUpperBounds();
            if (upperBoundsOther.length == upperBoundsThis.length) {
                boolean matches = true;
                for (int i = 0; i < upperBoundsThis.length && matches; i++) {
                    matches = isClassAssignableFrom(upperBoundsThis[i], upperBoundsOther[i]);
                }
                return matches;
            } else {
                return false;
            }
        }
        if (sourceType instanceof ParameterizedType) {
            sourceType = ((ParameterizedType) sourceType).getRawType();
        }
        Type finalSourceType = sourceType;
        return testBounds(
                wildcard.getUpperBounds(),
                upperBound -> isClassAssignableFrom(upperBound, finalSourceType));
    }

    /**
     * @return true if this wildcard can be a source for injection into a field of type {@code targetType}
     */
    public boolean targetMatches(Type targetType) {
        return testBounds(
                wildcard.getLowerBounds(),
                lowerBound -> isClassAssignableFrom(targetType, lowerBound));
    }

    private boolean testBounds(Type[] bounds, Predicate<Type> predicate) {
        boolean success = true;
        for (Type bound : bounds) {
            success = predicate.test(bound);
            if (!success) {
                break;
            }
        }
        return success;
    }

    private static boolean isClassAssignableFrom(Type bound, Type type) {
        return testClassTypes(bound, type, Class::isAssignableFrom);
    }

    private static boolean testClassTypes(
            Type type1, Type type2, BiPredicate<Class<?>, Class<?>> biPredicate) {
        return type1 instanceof Class
                && type2 instanceof Class
                && biPredicate.test((Class<?>) type1, (Class<?>) type2);
    }
}
