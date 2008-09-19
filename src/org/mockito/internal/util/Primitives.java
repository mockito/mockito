/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.HashMap;
import java.util.Map;

public class Primitives {
    
    public static boolean isPrimitiveWrapper(Class<?> type) {
        return wrapperReturnValues.containsKey(type);
    }
    
    public static Object primitiveWrapperOf(Class<?> type) {
        return wrapperReturnValues.get(type);
    }
    
    public static Class<?> primitiveTypeOf(Class<?> clazz) {
        return primitiveTypes.get(clazz);
    }
    
    private static Map<Class<?>, Object> wrapperReturnValues = new HashMap<Class<?>, Object>();
    private static Map<Class<?>, Class<?>> primitiveTypes = new HashMap<Class<?>, Class<?>>();
    
    static {
        wrapperReturnValues.put(Boolean.class, Boolean.FALSE);
        wrapperReturnValues.put(Character.class, new Character((char) 0));
        wrapperReturnValues.put(Byte.class, new Byte((byte) 0));
        wrapperReturnValues.put(Short.class, new Short((short) 0));
        wrapperReturnValues.put(Integer.class, new Integer(0));
        wrapperReturnValues.put(Long.class, new Long(0));
        wrapperReturnValues.put(Float.class, new Float(0));
        wrapperReturnValues.put(Double.class, new Double(0));
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
}