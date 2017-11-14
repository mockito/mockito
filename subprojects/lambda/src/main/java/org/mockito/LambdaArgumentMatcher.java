/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

public interface LambdaArgumentMatcher<T> extends ArgumentMatcher<T> {

    default T getValue() {
        return null;
    }

    /**
     * Whenever an invocation throws an IllegalArgumentException because `null` was not a valid value,
     * this is the case for non-nullness enforcing languages such as Kotlin, we have to construct an object instead.
     * By default, matchers do not need to implement this functionality. However, if there is limited information,
     * such as a {@link Class} (see {@link AnyClass}), constructing an object is still possible.
     * @return An object of type T, if it is possible to construct
     */
    default T constructObject() {
        throw new CouldNotConstructObjectException(this);
    }
}
