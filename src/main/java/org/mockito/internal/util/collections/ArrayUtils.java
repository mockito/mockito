/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

@SuppressWarnings("unchecked")
public class ArrayUtils {

    public <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

}