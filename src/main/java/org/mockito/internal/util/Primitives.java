/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Primitives {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPES;
    private static final Map<Class<?>, Object> PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES;

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
        return (Class<T>) PRIMITIVE_TYPES.get(clazz);
    }

    /**
     * Indicates if the given class is primitive type or a primitive wrapper.
     *
     * @param type The type to check
     * @return <code>true</code> if primitive or wrapper, <code>false</code> otherwise.
     */
    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.containsKey(type);
    }

    public static boolean isAssignableFromWrapper(Class<?> valueClass, Class<?> referenceType) {
        if (isPrimitiveOrWrapper(valueClass) && isPrimitiveOrWrapper(referenceType)) {
            return Primitives.primitiveTypeOf(valueClass)
                    .isAssignableFrom(Primitives.primitiveTypeOf(referenceType));
        }
        return false;
    }

    /**
     * Returns the boxed default value for a primitive or a primitive wrapper.
     *
     * @param primitiveOrWrapperType The type to lookup the default value
     * @return The boxed default values as defined in Java Language Specification,
     *         <code>null</code> if the type is neither a primitive nor a wrapper
     */
    public static <T> T defaultValue(Class<T> primitiveOrWrapperType) {
        return (T) PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.get(primitiveOrWrapperType);
    }

    static {
        Map<Class<?>, Class<?>> mutablePrimitiveTypes = new HashMap<>();
        mutablePrimitiveTypes.put(Boolean.class, Boolean.TYPE);
        mutablePrimitiveTypes.put(Character.class, Character.TYPE);
        mutablePrimitiveTypes.put(Byte.class, Byte.TYPE);
        mutablePrimitiveTypes.put(Short.class, Short.TYPE);
        mutablePrimitiveTypes.put(Integer.class, Integer.TYPE);
        mutablePrimitiveTypes.put(Long.class, Long.TYPE);
        mutablePrimitiveTypes.put(Float.class, Float.TYPE);
        mutablePrimitiveTypes.put(Double.class, Double.TYPE);
        PRIMITIVE_TYPES = Collections.unmodifiableMap(mutablePrimitiveTypes);
    }

    static {
        Map<Class<?>, Object> mutablePrimitiveOrWrapperDefaultValues = new HashMap<>();
        mutablePrimitiveOrWrapperDefaultValues.put(Boolean.class, false);
        mutablePrimitiveOrWrapperDefaultValues.put(Character.class, '\u0000');
        mutablePrimitiveOrWrapperDefaultValues.put(Byte.class, (byte) 0);
        mutablePrimitiveOrWrapperDefaultValues.put(Short.class, (short) 0);
        mutablePrimitiveOrWrapperDefaultValues.put(Integer.class, 0);
        mutablePrimitiveOrWrapperDefaultValues.put(Long.class, 0L);
        mutablePrimitiveOrWrapperDefaultValues.put(Float.class, 0F);
        mutablePrimitiveOrWrapperDefaultValues.put(Double.class, 0D);

        mutablePrimitiveOrWrapperDefaultValues.put(boolean.class, false);
        mutablePrimitiveOrWrapperDefaultValues.put(char.class, '\u0000');
        mutablePrimitiveOrWrapperDefaultValues.put(byte.class, (byte) 0);
        mutablePrimitiveOrWrapperDefaultValues.put(short.class, (short) 0);
        mutablePrimitiveOrWrapperDefaultValues.put(int.class, 0);
        mutablePrimitiveOrWrapperDefaultValues.put(long.class, 0L);
        mutablePrimitiveOrWrapperDefaultValues.put(float.class, 0F);
        mutablePrimitiveOrWrapperDefaultValues.put(double.class, 0D);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES =
                Collections.unmodifiableMap(mutablePrimitiveOrWrapperDefaultValues);
    }
}
