package org.mockito.internal.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
*
*/
public class InstanceField {
    private final Field field;
    private final Object instance;
    private FieldReader fieldReader;

    public InstanceField(Field field, Object instance) {
        this.field = field;
        this.instance = instance;
    }

    public Object read() {
        return reader().read();
    }

    public void set(Object value) {
        new FieldSetter(instance, field).set(value);
    }

    public boolean isNotNull() {
        return reader().isNull();
    }

    public boolean isAnnotatedBy(Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    public <A extends Annotation> A annotation(Class<A> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    public Field jdkField() {
        return field;
    }

    private FieldReader reader() {
        if (fieldReader == null) {
            fieldReader = new FieldReader(instance, field);
        }
        return fieldReader;
    }
}
