/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;
import static org.mockito.internal.util.StringUtil.decamelizeMatcherName;

import java.lang.reflect.Method;

import org.mockito.ArgumentMatcher;

/**
 * Provides better toString() text for matcher that don't have toString() method declared.
 */
class MatcherToString {

    /**
     * Attempts to provide more descriptive toString() for given matcher.
     * Searches matcher class hierarchy for toString() method. If it is found it will be used.
     * If no toString() is defined for the matcher hierarchy,
     * uses decamelized class name instead of default Object.toString().
     * This way we can promote meaningful names for custom matchers.
     *
     * @param matcher
     * @return
     */
    static String toString(ArgumentMatcher<?> matcher) {
        Class<?> cls = matcher.getClass();
        while (cls != Object.class) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method m : methods) {
                if (isToStringMethod(m)) {
                    return matcher.toString();
                }
            }
            cls = cls.getSuperclass();
        }

        String matcherName;
        Class<?> matcherClass = matcher.getClass();
        // Lambdas have non-empty getSimpleName() (despite being synthetic)
        // but that name is not useful for user
        if (matcherClass.isSynthetic()) {
            matcherName = "";
        } else {
            matcherName = matcherClass.getSimpleName();
        }

        return decamelizeMatcherName(matcherName);
    }
}
