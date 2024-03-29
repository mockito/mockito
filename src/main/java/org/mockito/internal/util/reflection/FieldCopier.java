package org.mockito.internal.util.reflection;

import org.mockito.plugins.MemberAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldCopier {

    private final MemberAccessor accessor;

    public FieldCopier(MemberAccessor accessor) {
        this.accessor = accessor;
    }

    public <T> void copyFields(T from, T to) {
        Class<?> fromClass = from.getClass();
        while (fromClass != Object.class) {
            copyValues(from, to, fromClass);
            fromClass = fromClass.getSuperclass();
        }
    }

    private <T> void copyValues(T from, T to, Class<?> classFrom) {
        Field[] fields = classFrom.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue; // ignore static fields
            }
            try {
                Object value = accessor.get(field, from);
                accessor.set(field, to, value);
            } catch (Throwable t) {
                // Ignore to be lenient
            }
        }
    }
}
