/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TypeSafeMatching implements ArgumentMatcherAction {

    private static final ArgumentMatcherAction TYPE_SAFE_MATCHING_ACTION = new TypeSafeMatching();

    /**
     * This cache is in theory unbounded. However, its max size is bounded by the number of types of argument matchers
     * that are both in the system and being used, which is expected to bound the cache's size to a low number
     * (<200) in all but the most contrived cases, and form a small percentage of the overall memory usage of those
     * classes.
     */
    private static final ConcurrentMap<Class<?>, Class<?>> argumentTypeCache =
            new ConcurrentHashMap<>();

    private TypeSafeMatching() {}

    public static ArgumentMatcherAction matchesTypeSafe() {
        return TYPE_SAFE_MATCHING_ACTION;
    }

    @Override
    public boolean apply(ArgumentMatcher matcher, Object argument) {
        return isCompatible(matcher, argument) && matcher.matches(argument);
    }

    /**
     * Returns <code>true</code> if the given <b>argument</b> can be passed to
     * the given <code>argumentMatcher</code> without causing a
     * {@link ClassCastException}.
     */
    private static boolean isCompatible(ArgumentMatcher<?> argumentMatcher, Object argument) {
        if (argument == null) {
            return true;
        }

        Class<?> expectedArgumentType = getArgumentType(argumentMatcher);

        return expectedArgumentType.isInstance(argument);
    }

    private static Class<?> getArgumentType(ArgumentMatcher<?> matcher) {
        Class<?> argumentMatcherType = matcher.getClass();
        Class<?> cached = argumentTypeCache.get(argumentMatcherType);
        // Avoids a lambda allocation on invocations >=2 for worse perf on invocation 1.
        if (cached != null) {
            return cached;
        } else {
            return argumentTypeCache.computeIfAbsent(
                    argumentMatcherType, unusedKey -> getArgumentTypeUncached(matcher));
        }
    }

    /**
     * Returns the type of {@link ArgumentMatcher#matches(Object)} of the given
     * {@link ArgumentMatcher} implementation.
     */
    private static Class<?> getArgumentTypeUncached(ArgumentMatcher<?> argumentMatcher) {
        Method[] methods = argumentMatcher.getClass().getMethods();

        for (Method method : methods) {
            if (isMatchesMethod(method)) {
                return method.getParameterTypes()[0];
            }
        }
        throw new NoSuchMethodError(
                "Method 'matches(T)' not found in ArgumentMatcher: "
                        + argumentMatcher
                        + " !\r\n Please file a bug with this stack trace at: https://github.com/mockito/mockito/issues/new ");
    }

    /**
     * Returns <code>true</code> if the given method is
     * {@link ArgumentMatcher#matches(Object)}
     */
    private static boolean isMatchesMethod(Method method) {
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        if (method.isBridge()) {
            return false;
        }
        return "matches".equals(method.getName());
    }
}
