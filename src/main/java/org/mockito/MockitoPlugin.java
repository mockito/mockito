package org.mockito;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

/**
 * TODO write some documentation 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MockitoPlugin {

    public Class<? extends AnnotationEngine> annotationEngine() default InjectingAnnotationEngine.class;
}
