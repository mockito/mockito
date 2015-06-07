/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

public class ArrayUtils {

    public <T> boolean isEmpty(final T[] array) {
        return array == null || array.length == 0;
    }

}