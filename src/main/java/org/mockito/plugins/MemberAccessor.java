/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import java.lang.reflect.*;

/**
 * A member accessor is responsible for invoking methods, constructors and for setting
 * and reading field values.
 */
public interface MemberAccessor {

    Object newInstance(Constructor<?> constructor, Object... arguments)
        throws InstantiationException, InvocationTargetException, IllegalAccessException;

    default Object newInstance(
        Constructor<?> constructor, OnConstruction onConstruction, Object... arguments)
        throws InstantiationException, InvocationTargetException, IllegalAccessException {
        return onConstruction.invoke(() -> newInstance(constructor, arguments));
    }

    Object invoke(Method method, Object target, Object... arguments)
        throws InvocationTargetException, IllegalAccessException;

    Object get(Field field, Object target) throws IllegalAccessException;

    void set(Field field, Object target, Object value) throws IllegalAccessException;

    interface OnConstruction {

        Object invoke(ConstructionDispatcher dispatcher)
            throws InstantiationException, InvocationTargetException, IllegalAccessException;
    }

    interface ConstructionDispatcher {

        Object newInstance()
            throws InstantiationException, InvocationTargetException, IllegalAccessException;
    }

    public default <T> void copyValues(T from, T mock, Class<?> classFrom) {
        Field[] fields = classFrom.getDeclaredFields();

        for (Field field : fields) {
            // ignore static fields6
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                Object value = this.get(field, from);
                this.set(field, mock, value);
            } catch (Throwable t) {
                // Ignore - be lenient - if some field cannot be copied then let's be it
            }
        }
    }
}
