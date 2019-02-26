/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.mockito.exceptions.base.MockitoException;

import static org.mockito.internal.util.reflection.AccessibilityChanger.enableAccess;

import java.lang.reflect.Field;

public class FieldReader {

    final Object target;
    final Field field;

    public FieldReader(Object target, Field field) {
        this.target = target;
        this.field = field;
        enableAccess(field);
    }

    public boolean isNull() {
            return read() == null;
    }

    public Object read() {
        try {
            return field.get(target);
        } catch (Exception e) {
            throw new MockitoException("Cannot read state from field: " + field + ", on instance: " + target);
        }
    }
}
