/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

/**
 * Pre-made preconditions
 */
public class Checks {

    public static <T> T checkNotNull(T value, String checkedValue) {
        return checkNotNull(value, checkedValue, null);
    }

    public static <T> T checkNotNull(T value, String checkedValue, String additionalMessage) {
        if (value == null) {
            String message = checkedValue + " should not be null";
            if (additionalMessage != null) {
                message += ". " + additionalMessage;
            }
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static <T extends Iterable<?>> T checkItemsNotNull(T iterable, String checkedIterable) {
        checkNotNull(iterable, checkedIterable);
        for (Object item : iterable) {
            checkNotNull(item, "item in " + checkedIterable);
        }
        return iterable;
    }
}
