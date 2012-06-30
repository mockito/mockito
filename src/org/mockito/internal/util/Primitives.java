/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Primitives {
    
    public static <T> Class<T> primitiveTypeOf(Class<T> clazz) {
        if(clazz.isPrimitive()) {
            return clazz;
        }
        return (Class<T>) primitiveTypes.get(clazz);
    }

    public static boolean isPrimitiveWrapper(Class<?> type) {
        return wrapperReturnValues.containsKey(type);
    }

    public static <T> T primitiveWrapperOf(Class<T> type) {
        return (T) wrapperReturnValues.get(type);
    }

    public static <T> T primitiveValueOrNullFor(Class<T> primitiveType) {
        return (T) primitiveValues.get(primitiveType);
    }
    private static Map<Class<?>, Class<?>> wrapperTypes = new HashMap<Class<?>, Class<?>>();
    private static Map<Class<?>, Class<?>> primitiveTypes = new HashMap<Class<?>, Class<?>>();
    private static Map<Class<?>, Object> wrapperReturnValues = new HashMap<Class<?>, Object>();
    private static Map<Class<?>, Object> primitiveValues = new HashMap<Class<?>, Object>();

    static {
        primitiveTypes.put(Boolean.class, Boolean.TYPE);
        primitiveTypes.put(Character.class, Character.TYPE);
        primitiveTypes.put(Byte.class, Byte.TYPE);
        primitiveTypes.put(Short.class, Short.TYPE);
        primitiveTypes.put(Integer.class, Integer.TYPE);
        primitiveTypes.put(Long.class, Long.TYPE);
        primitiveTypes.put(Float.class, Float.TYPE);
        primitiveTypes.put(Double.class, Double.TYPE);
    }

    static {
        wrapperReturnValues.put(Boolean.class, false);
        wrapperReturnValues.put(Character.class, '\u0000');
        wrapperReturnValues.put(Byte.class, (byte) 0);
        wrapperReturnValues.put(Short.class, (short) 0);
        wrapperReturnValues.put(Integer.class, 0);
        wrapperReturnValues.put(Long.class, 0L);
        wrapperReturnValues.put(Float.class, 0F);
        wrapperReturnValues.put(Double.class, 0D);
    }

    static {
        primitiveValues.put(boolean.class, false);
        primitiveValues.put(char.class, '\u0000');
        primitiveValues.put(byte.class, (byte) 0);
        primitiveValues.put(short.class, (short) 0);
        primitiveValues.put(int.class, 0);
        primitiveValues.put(long.class, 0L);
        primitiveValues.put(float.class, 0F);
        primitiveValues.put(double.class, 0D);
    }
}