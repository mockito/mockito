package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.AnnotationEngine;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DefaultAnntotationEngine implements AnnotationEngine {
    
    /* (non-Javadoc)
     * @see org.mockito.AnnotationEngine#createMockFor(java.lang.annotation.Annotation, java.lang.reflect.Field)
     */
    @SuppressWarnings("deprecation")
    public Object createMockFor(Annotation annotation, Field field) {
        if (annotation instanceof Mock || annotation instanceof org.mockito.MockitoAnnotations.Mock) {
            return Mockito.mock(field.getType(), field.getName());
        } else {
            return null;
        }
    }
}