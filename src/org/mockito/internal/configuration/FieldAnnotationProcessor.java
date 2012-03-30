package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Simple annotation processor interface.
 */
public interface FieldAnnotationProcessor<A extends Annotation> {
    Object process(A annotation, Field field);
}
