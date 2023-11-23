/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LenientCopyTool {

    MemberAccessor accessor = Plugins.getMemberAccessor();

    public <T> void copyToMock(T from, T mock) {
        copy(from, mock, from.getClass());
    }

    public <T> void copyToRealObject(T from, T to) {
        copy(from, to, from.getClass());
    }

    private <T> void copy(T from, T to, Class<?> fromClazz) {
        while (fromClazz != Object.class) {
            accessor.copyValues(from, to, fromClazz);
            fromClazz = fromClazz.getSuperclass();
        }
    }
}
