/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Whitebox {

    public static Object getInternalState(Object targetInstance, String fieldName) {
        return getInternalState(targetInstance.getClass(), targetInstance, fieldName);
    }

    public static Object getInternalState(Class targetClass, String fieldName) {
        return getInternalState(targetClass, null, fieldName);
    }

    private static Object getInternalState(Class<?> targetClass, Object targetInstance, String fieldName) {
        try {
            Field f = getFieldFromHierarchy(targetClass, fieldName);
            f.setAccessible(true);
            return f.get(targetInstance);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get internal state on a private field. Please report to mockito mailing list.", e);
        }
    }

    public static void setInternalState(Object targetInstance, String fieldName, Object value) {
        setInternalState(targetInstance.getClass(), targetInstance, fieldName, value);
    }

    public static void setInternalState(Class targetClass, String fieldName, Object value) {
        setInternalState(targetClass, null, fieldName, value);
    }

    private static void setInternalState(Class targetClass, Object targetInstance, String fieldName, Object value) {
        try {
            Field f = getFieldFromHierarchy(targetClass, fieldName);
            f.setAccessible(true);
            removeFinalModifierIfPresent(f);
            f.set(targetInstance, value);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set internal state on a private field. Please report to mockito mailing list.", e);
        }
    }

    private static void removeFinalModifierIfPresent(Field fieldToRemoveFinalFrom) throws IllegalAccessException, NoSuchFieldException {
        int fieldModifiersMask = fieldToRemoveFinalFrom.getModifiers();
        boolean isFinalModifierPresent = (fieldModifiersMask & Modifier.FINAL) == Modifier.FINAL;
        if (isFinalModifierPresent) {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            int fieldModifiersMaskWithoutFinal = fieldModifiersMask & ~Modifier.FINAL;
            modifiersField.setInt(fieldToRemoveFinalFrom, fieldModifiersMaskWithoutFinal);
        }
    }

    private static Field getFieldFromHierarchy(Class<?> clazz, String field) {
        Field f = getField(clazz, field);
        while (f == null && clazz != Object.class) {
            clazz = clazz.getSuperclass();
            f = getField(clazz, field);
        }
        if (f == null) {
            throw new RuntimeException(
                    "You want me to get this field: '" + field +
                    "' on this class: '" + clazz.getSimpleName() + 
                    "' but this field is not declared within the hierarchy of this class!");
        }
        return f;
    }

    private static Field getField(Class<?> clazz, String field) {
        try {
            return clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}