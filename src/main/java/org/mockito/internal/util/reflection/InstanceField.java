/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.mockito.internal.util.Checks;

import static org.mockito.internal.util.reflection.FieldSetter.setField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Represents an accessible instance field.
 *
 * Contains the instance reference on which the field can be read adn write.
 */
public class InstanceField {
    private final Field field;
    private final Object instance;
    private FieldReader fieldReader;

    /**
     * Create a new InstanceField.
     *
     * @param field The field that should be accessed, note that no checks are performed to ensure
     *              the field belong to this instance class.
     * @param instance The instance from which the field shall be accessed.
     */
    public InstanceField(Field field, Object instance) {
        this.field = Checks.checkNotNull(field, "field");
        this.instance = Checks.checkNotNull(instance, "instance");
    }

    /**
     * Safely read the field.
     *
     * @return the field value.
     * @see FieldReader
     */
    public Object read() {
        return reader().read();
    }

    /**
     * Set the given value to the field of this instance.
     *
     * @param value The value that should be written to the field.
     * @see FieldSetter
     */
    public void set(Object value) {
        setField(instance, field,value);
    }

    /**
     * Check that the field is not null.
     *
     * @return <code>true</code> if <code>null</code>, else <code>false</code>.
     */
    public boolean isNull() {
        return reader().isNull();
    }

    /**
     * Check if the field is annotated by the given annotation.
     *
     * @param annotationClass The annotation type to check.
     * @return <code>true</code> if the field is annotated by this annotation, else <code>false</code>.
     */
    public boolean isAnnotatedBy(Class<? extends Annotation> annotationClass) {
        return field.isAnnotationPresent(annotationClass);
    }

    /**
     * Check if the field is synthetic.
     *
     * @return <code>true</code> if the field is synthetic, else <code>false</code>.
     */
    public boolean isSynthetic() {
        return field.isSynthetic();
    }

    /**
     * Returns the annotation instance for the given annotation type.
     *
     * @param annotationClass Tha annotation type to retrieve.
     * @param <A> Type of the annotation.
     * @return The annotation instance.
     */
    public <A extends Annotation> A annotation(Class<A> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    /**
     * Returns the JDK {@link Field} instance.
     *
     * @return The actual {@link Field} instance.
     */
    public Field jdkField() {
        return field;
    }

    private FieldReader reader() {
        if (fieldReader == null) {
            fieldReader = new FieldReader(instance, field);
        }
        return fieldReader;
    }

    /**
     * Returns the name of the field.
     *
     * @return Name of the field.
     */
    public String name() {
        return field.getName();
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceField that = (InstanceField) o;
        return field.equals(that.field) && instance.equals(that.instance);
    }

    @Override
    public int hashCode() {
        int result = field.hashCode();
        result = 31 * result + instance.hashCode();
        return result;
    }
}
