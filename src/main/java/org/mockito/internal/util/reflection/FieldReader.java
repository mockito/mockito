package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

public class FieldReader {
    final Object target;
    final Field field;

    public FieldReader(Object target, Field field) {
        this.target = target;
        this.field = field;
    }

    public boolean isNull() {
        return read() == null;
    }

    public Object read() {
        try {
            return MemberAccessorProvider.getAccessor().get(field, target);
        } catch (Exception e) {
            throw new RuntimeException(
                "Cannot read state from field: " + field + ", on instance: " + target, e);
        }
    }
}
