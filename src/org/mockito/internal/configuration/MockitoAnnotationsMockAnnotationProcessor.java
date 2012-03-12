package org.mockito.internal.configuration;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;

import java.lang.reflect.Field;

/**
 * Instantiates a mock on a field annotated by {@link MockitoAnnotations.Mock}
 */
@SuppressWarnings("deprecation")
public class MockitoAnnotationsMockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {

    public Object process(MockitoAnnotations.Mock annotation, Field field) {
        return Mockito.mock(field.getType(), field.getName());
    }
}
