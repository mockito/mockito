/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class Primitives {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPES = new HashMap<>();
    private static final Map<Class<?>, Object> PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES =
            new HashMap<>();

    /** Older JVM environments do not have {@link Class#descriptorString()}, so build our own mapping. */
    private static final Map<Class<?>, Character> PRIMITIVE_DESCRIPTORS = new HashMap<>();

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

    /** Returns the code corresponding to the given primitive type according to JVMS 4.3.2. */
    public static char getPrimitiveDescriptor(Class<?> primitiveType) {
        return PRIMITIVE_DESCRIPTORS.get(primitiveType);
    }

    static {
        PRIMITIVE_TYPES.put(Boolean.class, Boolean.TYPE);
        PRIMITIVE_TYPES.put(Character.class, Character.TYPE);
        PRIMITIVE_TYPES.put(Byte.class, Byte.TYPE);
        PRIMITIVE_TYPES.put(Short.class, Short.TYPE);
        PRIMITIVE_TYPES.put(Integer.class, Integer.TYPE);
        PRIMITIVE_TYPES.put(Long.class, Long.TYPE);
        PRIMITIVE_TYPES.put(Float.class, Float.TYPE);
        PRIMITIVE_TYPES.put(Double.class, Double.TYPE);

        PRIMITIVE_DESCRIPTORS.put(boolean.class, 'Z');
        PRIMITIVE_DESCRIPTORS.put(char.class, 'C');
        PRIMITIVE_DESCRIPTORS.put(byte.class, 'B');
        PRIMITIVE_DESCRIPTORS.put(short.class, 'S');
        PRIMITIVE_DESCRIPTORS.put(int.class, 'I');
        PRIMITIVE_DESCRIPTORS.put(long.class, 'J');
        PRIMITIVE_DESCRIPTORS.put(float.class, 'F');
        PRIMITIVE_DESCRIPTORS.put(double.class, 'D');
    }

    static {
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Boolean.class, false);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Character.class, '\u0000');
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Byte.class, (byte) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Short.class, (short) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Integer.class, 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Long.class, 0L);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Float.class, 0F);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Double.class, 0D);

        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(boolean.class, false);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(char.class, '\u0000');
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(byte.class, (byte) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(short.class, (short) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(int.class, 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(long.class, 0L);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(float.class, 0F);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(double.class, 0D);
    }

    private Primitives() {}
}
