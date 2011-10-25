/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.*;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * See {@link MockitoAnnotations}
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class InjectingAnnotationEngine implements AnnotationEngine {
    
    AnnotationEngine delegate = new DefaultAnnotationEngine();
    AnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

    /***
     * Create a mock using {@link DefaultAnnotationEngine}
     *
     * @see org.mockito.internal.configuration.DefaultAnnotationEngine
     * @see org.mockito.configuration.AnnotationEngine#createMockFor(java.lang.annotation.Annotation, java.lang.reflect.Field)
     */
    @Deprecated
    public Object createMockFor(Annotation annotation, Field field) {
        return delegate.createMockFor(annotation, field);
    }

    /**
     * Process the fields of the test instance and create Mocks, Spies, Captors and inject them on fields
     * annotated &#64;InjectMocks.
     *
     * <p>
     * This code process the test class and the super classes.
     * <ol>
     * <li>First create Mocks, Spies, Captors.</li>
     * <li>Then try to inject them.</li>
     * </ol>
     *
     * @param clazz Not used
     * @param testInstance The instance of the test, should not be null.
     *
     * @see org.mockito.configuration.AnnotationEngine#process(Class, Object)
     */
    public void process(Class<?> clazz, Object testInstance) {
        processIndependentAnnotations(testInstance.getClass(), testInstance);
        processInjectMocks(testInstance.getClass(), testInstance);
    }

    private void processInjectMocks(final Class<?> clazz, final Object testInstance) {
        Class<?> classContext = clazz;
        while (classContext != Object.class) {
            injectMocks(testInstance);
            classContext = classContext.getSuperclass();
        }
    }

    private void processIndependentAnnotations(final Class<?> clazz, final Object testInstance) {
        Class<?> classContext = clazz;
        while (classContext != Object.class) {
            //this will create @Mocks, @Captors, etc:
            delegate.process(classContext, testInstance);
            //this will create @Spies:
            spyAnnotationEngine.process(classContext, testInstance);

            classContext = classContext.getSuperclass();
        }
    }

    void assertNoAnnotations(final Field field, final Class ... annotations) {
        for (Class annotation : annotations) {
            if (field.isAnnotationPresent(annotation)) {
                new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(), InjectMocks.class.getSimpleName());
            }
        }        
    }

    /**
     * Initializes mock/spies dependencies for objects annotated with
     * &#064;InjectMocks for given testClass.
     * <p>
     * See examples in javadoc for {@link MockitoAnnotations} class.
     * 
     * @param testClass
     *            Test class, usually <code>this</code>
     */
    public void injectMocks(final Object testClass) {
        Class<?> clazz = testClass.getClass();
        Set<Field> mockDependentFields = new HashSet<Field>();
        Set<Object> mocks = new HashSet<Object>();
        
        while (clazz != Object.class) {
            mockDependentFields.addAll(scanForInjection(testClass, clazz));
            mocks.addAll(scanMocks(testClass, clazz));
            clazz = clazz.getSuperclass();
        }
        
        new DefaultInjectionEngine().injectMocksOnFields(mockDependentFields, mocks, testClass);
    }

    /**
     * Scan fields annotated by &#064;InjectMocks
     *
     * @param testClass
     * @param clazz
     * @return
     */
    private Set<Field> scanForInjection(final Object testClass, final Class<?> clazz) {
        Set<Field> mockDependentFields = new HashSet<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(InjectMocks.class)) {
                assertNoAnnotations(field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
                mockDependentFields.add(field);
            }
        }

        return mockDependentFields;
    }

    private Set<Object> scanMocks(final Object testClass, final Class<?> clazz) {
        Set<Object> mocks = new HashSet<Object>();
        for (Field field : clazz.getDeclaredFields()) {
            // mock or spies only
            if (null != field.getAnnotation(Spy.class) || null != field.getAnnotation(org.mockito.Mock.class)
                    || null != field.getAnnotation(org.mockito.MockitoAnnotations.Mock.class)) {
                Object fieldInstance = null;
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    fieldInstance = field.get(testClass);
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Problems reading this field dependency " + field.getName() + " for injection", e);
                } finally {
                    field.setAccessible(wasAccessible);
                }
                if (fieldInstance != null) {
                    mocks.add(fieldInstance);
                }
            }
        }
        return mocks;
    }
}
