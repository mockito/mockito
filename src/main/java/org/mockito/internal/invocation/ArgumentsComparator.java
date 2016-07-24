/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;

@SuppressWarnings("unchecked")
public class ArgumentsComparator {

    private ArgumentsComparator() {}

    public static boolean argumentsMatch(InvocationMatcher invocationMatcher, Invocation actual) {
        Object[] actualArgs = actual.getArguments();
        return argumentsMatch(invocationMatcher, actualArgs) || varArgsMatch(invocationMatcher, actual);
    }

    public static boolean argumentsMatch(InvocationMatcher invocationMatcher, Object[] actualArgs) {
        if (actualArgs.length != invocationMatcher.getMatchers().size()) {
            return false;
        }
        for (int i = 0; i < actualArgs.length; i++) {
            ArgumentMatcher<Object> argumentMatcher = invocationMatcher.getMatchers().get(i);
            Object argument = actualArgs[i];

            if (!matches(argumentMatcher, argument)) {
                return false;
            }
        }
        return true;
    }

    // ok, this method is a little bit messy but the vararg business unfortunately is messy...
    private static boolean varArgsMatch(InvocationMatcher invocationMatcher, Invocation actual) {
        if (!actual.getMethod().isVarArgs()) {
            // if the method is not vararg forget about it
            return false;
        }

        // we must use raw arguments, not arguments...
        Object[] rawArgs = actual.getRawArguments();
        List<ArgumentMatcher> matchers = invocationMatcher.getMatchers();

        if (rawArgs.length != matchers.size()) {
            return false;
        }

        for (int i = 0; i < rawArgs.length; i++) {
            ArgumentMatcher m = matchers.get(i);
            // it's a vararg because it's the last array in the arg list
            if (rawArgs[i] != null && rawArgs[i].getClass().isArray() && i == rawArgs.length - 1) {
                // this is very important to only allow VarargMatchers here. If
                // you're not sure why remove it and run all tests.
                if (!(m instanceof VarargMatcher) || !m.matches(rawArgs[i])) {
                    return false;
                }
                // it's not a vararg (i.e. some ordinary argument before
                // varargs), just do the ordinary check
            } else if (!m.matches(rawArgs[i])) {
                return false;
            }
        }

        return true;
    }

    private static boolean matches(ArgumentMatcher<Object> argumentMatcher, Object argument) {
        return isCompatible(argumentMatcher, argument) && argumentMatcher.matches(argument);
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
        return method.getName().equals("matches");
    }
}
