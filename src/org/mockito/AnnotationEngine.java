package org.mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.internal.configuration.DefaultAnntotationEngine;

/**
 * Configures mock creation logic behind &#064;Mock annotations
 * <p>
 * See how it is implemented in {@link DefaultAnntotationEngine} and {@link MockitoAnnotations#initMocks(Object)}
 */
public interface AnnotationEngine {

    /**
     * @param annotation
     * @param field
     * @return
     */
    Object createMockFor(Annotation annotation, Field field);

}