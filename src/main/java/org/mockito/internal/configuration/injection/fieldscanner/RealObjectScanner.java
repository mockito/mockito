package org.mockito.internal.configuration.injection.fieldscanner;

import org.mockito.*;
import org.mockito.internal.configuration.injection.RealObject;
import org.mockito.internal.util.reflection.FieldReader;

import java.lang.reflect.Field;

/**
 * Scan fields for the {@link Real} annotation.
 */
public class RealObjectScanner extends FieldScanner<Object> {

    private Object objectInstance;

    public RealObjectScanner(Object testClassInstance, Class<?> clazz) {
        super(testClassInstance, clazz);
    }

    @Override
    protected boolean hasCorrectAnnotation(Field field) {
        this.objectInstance = new FieldReader(this.testClassInstance, field).read();

        return null != objectInstance
            && (field.getAnnotation(Real.class) != null
                || field.getAnnotation(InjectMocks.class) != null);
    }

    @Override
    protected Object getObjectToAdd(Field field) {
        if (null != field.getAnnotation(Real.class)) {
            assertNoAnnotations(Real.class, field, Mock.class, MockitoAnnotations.Mock.class, Spy.class);
        }

        return new RealObject(objectInstance, field.getName());
    }
}
