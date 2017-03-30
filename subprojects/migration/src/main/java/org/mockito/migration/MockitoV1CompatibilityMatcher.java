/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.migration;

import java.lang.reflect.Method;
import org.mockito.ArgumentMatcher;
import org.mockito.Incubating;

/**
 * Base class for classes that implement {@link ArgumentMatcher}.
 * <p>
 * This was provided to simplify migration from pre 2.x implementations; it resolves the conflict
 * caused by the signature of the {@code matches(...)} method changing from {@code matches(Object)}
 * in pre 2.x to {@code matches(T)} by adding a new {@link #matchesSafely(T)} method which
 * developers implement instead. This checks the type of the
 * argument beforehand and returns false if it is not of the correct type to avoid a
 * {@link ClassCastException} being thrown.
 * <p>
 * It does not provide a default implementation of {@link #toString()} as if no {@link #toString()}
 * method is provided then it defaults to the pre 2.x behavior of creating a decamelized form of the
 * class name.
 *
 * @deprecated this was only intended to ease migration from 1.x to 2.x please implement
 * {@link ArgumentMatcher} directly.
 */
@Incubating
@Deprecated
public abstract class MockitoV1CompatibilityMatcher<T> extends ArgumentMatcher<T> {

    /**
     * Informs if this matcher accepts the given argument.
     * <p>
     * It is marked as final to prevent it being overridden because the method clashes with the
     * equivalent method in the 1.x version of {@link ArgumentMatcher}. ImpleTypeSafeMatchingment
     * {@link #matchesSafely(Object)} instead.
     *
     * @param argument the argument
     * @return true if this matcher accepts the given argument.
     */
    @Override
    public final boolean matches(Object argument) {
        Class<T> argumentType = getArgumentType(this);
        return argumentType.isInstance(argument) && matchesSafely(argumentType.cast(argument));
    }

    /**
     * Informs if this matcher accepts the given argument.
     * <p>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     *
     * @param argument the argument
     * @return true if this matcher accepts the given argument.
     */
    public abstract boolean matchesSafely(T argument);

    /**
     * Returns the type of {@link ArgumentMatcher#matches(Object)} of the given
     * {@link ArgumentMatcher} implementation.
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> getArgumentType(ArgumentMatcher<T> argumentMatcher) {
        Method[] methods = argumentMatcher.getClass().getMethods();

        for (Method method : methods) {
            if (isMatchesMethod(method)) {
                return (Class<T>) method.getParameterTypes()[0];
            }
        }
        throw new NoSuchMethodError("Method '" + "matchesSafely" + "(T)' not found in ArgumentMatcher: "
            + argumentMatcher + " !\r\n Please file a bug with this stack trace at: https://github.com/mockito/mockito/issues/new ");
    }

    /**
     * Returns <code>true</code> if the given method is
     * {@link MockitoV1CompatibilityMatcher#matchesSafely(Object)}
     */
    private static boolean isMatchesMethod(Method method) {
        return method.getParameterTypes().length == 1 && !method.isBridge()
            && method.getName().equals("matchesSafely");
    }
}
