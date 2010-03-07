/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

@SuppressWarnings("unchecked")
public class ArrayUtils {
    public Class<?>[] concat(Class<?>[] interfaces, Class<?> clazz) {
        int length = interfaces.length;
        Class[] out = new Class[length +1];
        System.arraycopy(interfaces, 0, out, 0, length);
        out[length] = clazz;
        return out;
    }
}