/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.Decamelizer;
import org.mockito.internal.util.ObjectMethodsGuru;

import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.lang.reflect.Method;

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
        while(cls != Object.class) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method m : methods) {
                if(isToStringMethod(m)) {
                    return matcher.toString();
                }
            }
            cls = cls.getSuperclass();
        }
        return Decamelizer.decamelizeMatcher(matcher.getClass().getSimpleName());
    }
}
