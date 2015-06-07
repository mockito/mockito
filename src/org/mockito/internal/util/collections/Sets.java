/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;


import static java.util.Arrays.asList;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Sets {
    public static Set<Object> newMockSafeHashSet(final Iterable<Object> mocks) {
        return HashCodeAndEqualsSafeSet.of(mocks);
    }

    public static Set<Object> newMockSafeHashSet(final Object... mocks) {
        return HashCodeAndEqualsSafeSet.of(mocks);
    }

    public static IdentitySet newIdentitySet() {
        return new IdentitySet();
    }

    public static <T> Set<T> newSet(final T ... elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Expected an array of elements (or empty array) but received a null.");
        }
        return new LinkedHashSet<T>(asList(elements));
    }
}
