/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.mockito.internal.exceptions.Reporter.moreThanOneAnnotationNotAllowed;
import static org.mockito.internal.util.reflection.FieldSetter.setField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.plugins.AnnotationEngine;

/**
 * Initializes fields annotated with &#64;{@link org.mockito.Mock} or &#64;{@link org.mockito.Captor}.
 *
 * <p>
 * The {@link #process(Class, Object)} method implementation <strong>does not</strong> process super classes!
 *
 * @see MockitoAnnotations
 */
@SuppressWarnings("unchecked")
public class IndependentAnnotationEngine
        implements AnnotationEngine, org.mockito.configuration.AnnotationEngine {
    private final Map<Class<? extends Annotation>, FieldAnnotationProcessor<?>>
            annotationProcessorMap =
                    new HashMap<Class<? extends Annotation>, FieldAnnotationProcessor<?>>();

    public IndependentAnnotationEngine() {
        registerAnnotationProcessor(Mock.class, new MockAnnotationProcessor());
        registerAnnotationProcessor(Captor.class, new CaptorAnnotationProcessor());
    }

    private Object createMockFor(Annotation annotation, Field field) {
        return forAnnotation(annotation).process(annotation, field);
    }

    private <A extends Annotation> FieldAnnotationProcessor<A> forAnnotation(A annotation) {
        if (annotationProcessorMap.containsKey(annotation.annotationType())) {
            return (FieldAnnotationProcessor<A>)
                    annotationProcessorMap.get(annotation.annotationType());
        }
        return new FieldAnnotationProcessor<A>() {
            public Object process(A annotation, Field field) {
                return null;
            }
        };
    }

    private <A extends Annotation> void registerAnnotationProcessor(
            Class<A> annotationClass, FieldAnnotationProcessor<A> fieldAnnotationProcessor) {
        annotationProcessorMap.put(annotationClass, fieldAnnotationProcessor);
    }

    @Override
    public AutoCloseable process(Class<?> clazz, Object testInstance) {
        List<MockedStatic<?>> mockedStatics = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean alreadyAssigned = false;
            for (Annotation annotation : field.getAnnotations()) {
                Object mock = createMockFor(annotation, field);
                if (mock instanceof MockedStatic<?>) {
                    mockedStatics.add((MockedStatic<?>) mock);
                }
                if (mock != null) {
                    throwIfAlreadyAssigned(field, alreadyAssigned);
                    alreadyAssigned = true;
                    try {
                        setField(testInstance, field, mock);
                    } catch (Exception e) {
                        for (MockedStatic<?> mockedStatic : mockedStatics) {
                            mockedStatic.close();
                        }
                        throw new MockitoException(
                                "Problems setting field "
                                        + field.getName()
                                        + " annotated with "
                                        + annotation,
                                e);
                    }
                }
            }
        }
        return () -> {
            for (MockedStatic<?> mockedStatic : mockedStatics) {
                mockedStatic.closeOnDemand();
            }
        };
    }

    void throwIfAlreadyAssigned(Field field, boolean alreadyAssigned) {
        if (alreadyAssigned) {
            throw moreThanOneAnnotationNotAllowed(field.getName());
        }
    }
}
