/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

@SuppressWarnings("unchecked")
public class ArrayUtils {

    // TODO don't use typed arguments ?
    public Class<?>[] concat(Class<?>[] interfaces, Class<?>... clazz) {
        int interfacesCount = interfaces.length;
        int appendedCount = clazz.length;
        Class[] out = new Class[interfacesCount + appendedCount];
        System.arraycopy(interfaces, 0, out, 0, interfacesCount);
        System.arraycopy(clazz, 0, out, interfacesCount, appendedCount);
        return out;
    }

    public <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

}