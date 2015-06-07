/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

/**
 * Pre-made preconditions
 */
@SuppressWarnings("rawtypes")
public class Checks {

    public static <T> T checkNotNull(final T value, final String checkedValue) {
        if(value == null) {
            throw new NullPointerException(checkedValue + " should not be null");
        }
        return value;
    }

    public static <T extends Iterable> T checkItemsNotNull(final T iterable, final String checkedIterable) {
        checkNotNull(iterable, checkedIterable);
        for (final Object item : iterable) {
            checkNotNull(item, "item in " + checkedIterable);
        }
        return iterable;
    }
}
