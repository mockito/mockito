/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

public final class FieldSetter {

    private FieldSetter() {}

    /**
     * Set value of fields, except static final (use {@link #setAnyField(Object, Field, Object)} for that).
     */
    public static void setField(Object target, Field field, Object value) {
        AccessibilityChanger changer = new AccessibilityChanger();
        changer.enableAccess(field);

        try {

            field.set(target, value);

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Access not authorized on field '" + field + "' of object '" + target + "'"
                + " with value: '" + value + "'", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Wrong argument on field '" + field + "' of object '" + target + "' with "
                + "value: '" + value + "', \n" +
                "reason : " + e.getMessage(), e);
        } finally {
            changer.safelyDisableAccess(field);
        }
    }

    /**
     * Compared to {@link #setField(Object, Field, Object)}, this method also allows setting static final fields.
     *
     * Setting static final fields needs some mangling on the field modifiers of the {@link Field} class and thus should
     * only be used when absolutely necessary. See {@link StaticFinalOverrider} for implementation details.
     */
    public static void setAnyField(Object target, Field field, Object value) {
        synchronized (field) {
            StaticFinalOverrider overrider = new StaticFinalOverrider();
            overrider.enableWrite(field);

            try {

                setField(target, field, value);

            } finally {
                overrider.safelyDisableWrite(field);
            }
        }
    }
}
