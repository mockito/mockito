/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.plugins.AnnotationEngine;

import static org.mockito.internal.exceptions.Reporter.unsupportedCombinationOfAnnotations;

/**
 * Process fields annotated with &#64;Spy.
 * <p>
 * Will try transform the field in a spy as with <code>Mockito.spy()</code>.
 * </p>
 * <p>
 * If the field is not initialized, will try to initialize it, with a no-arg constructor.
 * </p>
 * <p>
 * If the field is also annotated with the <strong>compatible</strong> &#64;InjectMocks then the field will be ignored,
 * The injection engine will handle this specific case.
 * </p>
 * <p>This engine will fail, if the field is also annotated with incompatible Mockito annotations.
 */
@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngine implements AnnotationEngine, org.mockito.configuration.AnnotationEngine {

    @Override
    public void process(Class<?> context, Object testInstance) {
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            // Combination of @InjectMocks and @Spy have to be handled separately by the injection engine
            if (field.isAnnotationPresent(Spy.class) && !field.isAnnotationPresent(InjectMocks.class)) {
                assertNoIncompatibleAnnotations(Spy.class, field, Mock.class, Captor.class);
                SpyFieldInitializer.initializeSpy(testInstance, field);
            }
        }
    }

    //TODO duplicated elsewhere
    private static void assertNoIncompatibleAnnotations(Class<? extends Annotation> annotation,
                                                        Field field,
                                                        Class<? extends Annotation>... undesiredAnnotations) {
        for (Class<? extends Annotation> u : undesiredAnnotations) {
            if (field.isAnnotationPresent(u)) {
                throw unsupportedCombinationOfAnnotations(annotation.getSimpleName(),
                                                          annotation.getClass().getSimpleName());
            }
        }
    }
}
