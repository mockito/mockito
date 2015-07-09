/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

public class FieldCopier {

    public <T> void copyValue(T from, T to, Field field) throws IllegalAccessException {
        Object value = field.get(from);
        field.set(to, value);
    }
}
