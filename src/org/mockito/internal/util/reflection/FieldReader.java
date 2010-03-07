/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

import org.mockito.exceptions.base.MockitoException;

public class FieldReader {

    final Object target;
    final Field field;
    final AccessibilityChanger changer = new AccessibilityChanger();

    public FieldReader(Object target, Field field) {
        this.target = target;
        this.field = field;
        changer.enableAccess(field);
    }

    public boolean isNull() {
        try {
            return field.get(target) == null;
        } catch (Exception e) {
            throw new MockitoException("Cannot read state from field: " + field + ", on instance: " + target);
        }
    }
}
