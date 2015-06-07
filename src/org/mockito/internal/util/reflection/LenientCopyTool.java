/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LenientCopyTool {

    FieldCopier fieldCopier = new FieldCopier();

    public <T> void copyToMock(final T from, final T mock) {
        copy(from, mock, from.getClass(), mock.getClass().getSuperclass());
    }

    public <T> void copyToRealObject(final T from, final T to) {
        copy(from, to, from.getClass(), to.getClass());
    }

    private <T> void copy(final T from, final T to, Class fromClazz, final Class toClass) {
        while (fromClazz != Object.class) {
            copyValues(from, to, fromClazz);
            fromClazz = fromClazz.getSuperclass();
        }
    }

    private <T> void copyValues(final T from, final T mock, final Class classFrom) {
        final Field[] fields = classFrom.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            // ignore static fields
            final Field field = fields[i];
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            final AccessibilityChanger accessibilityChanger = new AccessibilityChanger();
            try {
                accessibilityChanger.enableAccess(field);
                fieldCopier.copyValue(from, mock, field);
            } catch (final Throwable t) {
                //Ignore - be lenient - if some field cannot be copied then let's be it
            } finally {
                accessibilityChanger.safelyDisableAccess(field);
            }
        }
    }
}