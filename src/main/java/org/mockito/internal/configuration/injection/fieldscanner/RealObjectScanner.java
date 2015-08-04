package org.mockito.internal.configuration.injection.fieldscanner;

import org.mockito.Real;
import org.mockito.internal.configuration.injection.RealObject;
import org.mockito.internal.util.reflection.FieldReader;

import java.lang.reflect.Field;

/**
 * Scan fields for the {@link Real} annotation.
 */
public class RealObjectScanner extends FieldScanner<Object> {

    public RealObjectScanner(Object testClassInstance, Class<?> clazz) {
        super(testClassInstance, clazz);
    }

    @Override
    protected boolean hasCorrectAnnotation(Field field) {
        return field.getAnnotation(Real.class) != null;
    }

    @Override
    protected Object getObjectToAdd(Field field) {
        return new RealObject(
                new FieldReader(this.testClassInstance, field).read(),
                field.getName());
    }
}
