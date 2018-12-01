/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * To set a value on a static final field, setting the field accessible
 * (i.e.: {@link Field#setAccessible(boolean)}) is not enough.
 * Only if removing the field modifier {@link Modifier#STATIC}, a value can be set via reflection.
 */
public class StaticFinalOverrider {
    private Field modifiersField = null;

    public void enableWrite(Field field) {
        if ((field.getModifiers() & (Modifier.FINAL + Modifier.STATIC)) == 0) {
            return;
        }

        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("modifiers field not found on field: " + field, e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot change modifier on field: " + field, e);
        }
    }

    public void safelyDisableWrite(Field field) {
        if (modifiersField == null) {
            return;
        }

        try {
            modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
            modifiersField.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot reset static final field: " + field, e);
        }

    }
}
