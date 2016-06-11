/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

public class FieldSetter {

    private FieldSetter(){}

    public static void setField(Object target, Field field,Object value) {
        AccessibilityChanger changer = new AccessibilityChanger();
        changer.enableAccess(field);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Access not authorized on field '" + field + "' of object '" + target + "' with value: '" + value + "'", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Wrong argument on field '" + field + "' of object '" + target + "' with value: '" + value + "', \n" +
                    "reason : " + e.getMessage(), e);
        }
        changer.safelyDisableAccess(field);
    }
}
