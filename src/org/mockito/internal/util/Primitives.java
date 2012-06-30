/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Primitives {

    private static Map<Class<?>, Class<?>> primitiveTypes = new HashMap<Class<?>, Class<?>>();
    private static Map<Class<?>, Object> primitiveOrWrapperDefaultValues = new HashMap<Class<?>, Object>();


    /**
     * Returns the primitive type of the given class.
     * <p/>
     * The passed class can be any class : <code>boolean.class</code>, <code>Integer.class</code>
     * in witch case this method will return <code>boolean.class</code>, even <code>SomeObject.class</code>
     * in which case <code>null</code> will be returned.
     *
     * @param clazz The class from which primitive type has to be retrieved
     * @param <T>   The type
     * @return The primitive type if relevant, otherwise <code>null</code>
     */
    public static <T> Class<T> primitiveTypeOf(Class<T> clazz) {
        if (clazz.isPrimitive()) {
            return clazz;
        }
        return (Class<T>) primitiveTypes.get(clazz);
    }

    /**
     * Indicates if the given class is primitive type or a primitive wrapper.
     *
     * @param type The type to check
     * @return <code>true</code> if primitive or wrapper, <code>false</code> otherwise.
     */
    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return primitiveOrWrapperDefaultValues.containsKey(type);
    }

    /**
     * Returns the boxed default value for a primitive or a primitive wrapper.
     *
     * @param primitiveOrWrapperType The type to lookup the default value
     * @return The boxed default values as defined in Java Language Specification,
     *         <code>null</code> if the type is neither a primitive nor a wrapper
     */
    public static <T> T defaultValueForPrimitiveOrWrapper(Class<T> primitiveOrWrapperType) {
        return (T) primitiveOrWrapperDefaultValues.get(primitiveOrWrapperType);
    }


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
        primitiveOrWrapperDefaultValues.put(Boolean.class, false);
        primitiveOrWrapperDefaultValues.put(Character.class, '\u0000');
        primitiveOrWrapperDefaultValues.put(Byte.class, (byte) 0);
        primitiveOrWrapperDefaultValues.put(Short.class, (short) 0);
        primitiveOrWrapperDefaultValues.put(Integer.class, 0);
        primitiveOrWrapperDefaultValues.put(Long.class, 0L);
        primitiveOrWrapperDefaultValues.put(Float.class, 0F);
        primitiveOrWrapperDefaultValues.put(Double.class, 0D);

        primitiveOrWrapperDefaultValues.put(boolean.class, false);
        primitiveOrWrapperDefaultValues.put(char.class, '\u0000');
        primitiveOrWrapperDefaultValues.put(byte.class, (byte) 0);
        primitiveOrWrapperDefaultValues.put(short.class, (short) 0);
        primitiveOrWrapperDefaultValues.put(int.class, 0);
        primitiveOrWrapperDefaultValues.put(long.class, 0L);
        primitiveOrWrapperDefaultValues.put(float.class, 0F);
        primitiveOrWrapperDefaultValues.put(double.class, 0D);
    }
}