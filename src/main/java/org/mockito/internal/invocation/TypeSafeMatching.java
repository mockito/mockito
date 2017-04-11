/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;

import org.mockito.ArgumentMatcher;

@SuppressWarnings({"unchecked","rawtypes"})
public class TypeSafeMatching implements ArgumentMatcherAction {

    private final static ArgumentMatcherAction TYPE_SAFE_MATCHING_ACTION = new TypeSafeMatching();

    private TypeSafeMatching() {}


    public static ArgumentMatcherAction matchesTypeSafe(){
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
        if (argument == null)
            return true;

        Class<?> expectedArgumentType = getArgumentType(argumentMatcher);

        return expectedArgumentType.isInstance(argument);
    }

    /**
     * Returns the type of {@link ArgumentMatcher#matches(Object)} of the given
     * {@link ArgumentMatcher} implementation.
     */
    private static Class<?> getArgumentType(ArgumentMatcher<?> argumentMatcher) {
        Method[] methods = argumentMatcher.getClass().getMethods();

        for (Method method : methods) {
            if (isMatchesMethod(method)) {
                return method.getParameterTypes()[0];
            }
        }
        throw new NoSuchMethodError("Method 'matches(T)' not found in ArgumentMatcher: " + argumentMatcher + " !\r\n Please file a bug with this stack trace at: https://github.com/mockito/mockito/issues/new ");
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
