/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.*;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.mockito.Mockito.withSettings;

/**
 * Process fields annotated with &#64;Spy.
 *
 * <p>
 * Will try transform the field in a spy as with <code>Mockito.spy()</code>.
 * </p>
 *
 * <p>
 * If the field is not initialized, will try to initialize it, with a no-arg constructor.
 * </p>
 *
 * <p>
 * If the field is also annotated with the <strong>compatible</strong> &#64;InjectMocks then the field will be ignored,
 * The injection engine will handle this specific case.
 * </p>
 *
 * <p>This engine will fail, if the field is also annotated with incompatible Mockito annotations.
 */
@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngine implements AnnotationEngine {

    public Object createMockFor(Annotation annotation, Field field) {
        return null;
    }

    @SuppressWarnings("deprecation") // for MockitoAnnotations.Mock
    public void process(Class<?> context, Object testInstance) {
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class) && !field.isAnnotationPresent(InjectMocks.class)) {
                assertNoIncompatibleAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
                Object instance = null;
                try {
                    FieldInitializationReport report = new FieldInitializer(testInstance, field).initialize();
                    instance = report.fieldInstance();
                } catch (MockitoException e) {
                    new Reporter().cannotInitializeForSpyAnnotation(field.getName(), e);
                }
                try {
                    if (new MockUtil().isMock(instance)) {
                        // instance has been spied earlier
                        // for example happens when MockitoAnnotations.initMocks is called two times.
                        Mockito.reset(instance);
                    } else {
                        field.setAccessible(true);
                        field.set(testInstance, Mockito.mock(instance.getClass(), withSettings()
                                .spiedInstance(instance)
                                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
                                .name(field.getName())));
                    }
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Problems initiating spied field " + field.getName(), e);
                }
            }
        }
    }
    
    //TODO duplicated elsewhere
    void assertNoIncompatibleAnnotations(Class annotation, Field field, Class... undesiredAnnotations) {
        for (Class u : undesiredAnnotations) {
            if (field.isAnnotationPresent(u)) {
                new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(), annotation.getClass().getSimpleName());
            }
        }        
    }    
}
