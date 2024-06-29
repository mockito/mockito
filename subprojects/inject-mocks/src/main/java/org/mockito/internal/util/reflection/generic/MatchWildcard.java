/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MatchWildcard implements MatchType {

    private final WildcardType wildcard;
    private final boolean captured;

    protected MatchWildcard(WildcardType wildcard, boolean captured) {
        this.wildcard = wildcard;
        this.captured = captured;
    }

    @Override
    public boolean matches(MatchType other) {
        if (!captured && other instanceof MatchWildcard) {
            MatchWildcard matchWildcardOther = (MatchWildcard) other;
            Type[] upperBoundsThis = this.wildcard.getUpperBounds();
            Type[] upperBoundsOther = matchWildcardOther.wildcard.getUpperBounds();
            if (upperBoundsOther.length == upperBoundsThis.length) {
                boolean matches = true;
                for (int i = 0; i < upperBoundsThis.length && matches; i++) {
                    matches = isUpperBoundAssignableFrom(upperBoundsThis[i], upperBoundsOther[i]);
                }
                return matches;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    static MatchType ofWildcardType(WildcardType wildcardType) {
        return new MatchWildcard(wildcardType, false);
    }

    MatchType makeCaptured() {
        return this.captured ? this : new MatchWildcard(wildcard, true);
    }

    public boolean matches(Type type) {
        return !captured
                && testUpperBounds(upperBound -> isUpperBoundAssignableFrom(upperBound, type));
    }

    public boolean targetMatches(Type targetType) {
        return testUpperBounds(upperBound -> isUpperBoundAssignableTo(upperBound, targetType));
    }

    private boolean testUpperBounds(Predicate<Type> predicate) {
        boolean success = true;
        for (Type upperBound : wildcard.getUpperBounds()) {
            success = predicate.test(upperBound);
            if (!success) {
                break;
            }
        }
        return success;
    }

    private static boolean isUpperBoundAssignableFrom(Type upperBound, Type type) {
        return testClassTypes(upperBound, type, Class::isAssignableFrom);
    }

    private static boolean isUpperBoundAssignableTo(Type upperBound, Type type) {
        return testClassTypes(upperBound, type, reverse(Class::isAssignableFrom));
    }

    private static boolean testClassTypes(
            Type upperBound, Type type, BiPredicate<Class<?>, Class<?>> biPredicate) {
        return upperBound instanceof Class
                && type instanceof Class
                && biPredicate.test((Class<?>) upperBound, (Class<?>) type);
    }

    private static <T> BiPredicate<T, T> reverse(BiPredicate<T, T> biPredicate) {
        return (t1, t2) -> biPredicate.test(t2, t1);
    }
}
