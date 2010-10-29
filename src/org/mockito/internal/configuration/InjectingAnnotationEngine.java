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
    
    /* (non-Javadoc)
    * @see org.mockito.AnnotationEngine#createMockFor(java.lang.annotation.Annotation, java.lang.reflect.Field)
    */    
    public Object createMockFor(Annotation annotation, Field field) {
        return delegate.createMockFor(annotation, field);
    }
    
    public void process(Class<?> context, Object testClass) {
        //this will create @Mocks, @Captors, etc:
        delegate.process(context, testClass);
        //this will create @Spies:
        spyAnnotationEngine.process(context, testClass);
        
        //this injects mocks
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectMocks.class)) {
                assertNoAnnotations(field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
                injectMocks(testClass);
            }
        }
    } 
    
    void assertNoAnnotations(Field field, Class ... annotations) {
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
    public void injectMocks(Object testClass) {       
        Class<?> clazz = testClass.getClass();
        Set<Field> mockDependents = new HashSet<Field>();
        Set<Object> mocks = new HashSet<Object>();
        
        while (clazz != Object.class) {
            mockDependents.addAll(scanForInjection(testClass, clazz));
            mocks.addAll(scanMocks(testClass, clazz));
            clazz = clazz.getSuperclass();
        }
        
        new DefaultInjectionEngine().injectMocksOnFields(mockDependents, mocks, testClass);
    }

    private static Set<Field> scanForInjection(Object testClass, Class<?> clazz) {
        Set<Field> testedFields = new HashSet<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(InjectMocks.class)) {
                testedFields.add(field);
            }
        }

        return testedFields;
    }

    private static Set<Object> scanMocks(Object testClass, Class<?> clazz) {
        Set<Object> mocks = new HashSet<Object>();
        for (Field field : clazz.getDeclaredFields()) {
            // mock or spies only
            if (null != field.getAnnotation(Spy.class) || null != field.getAnnotation(org.mockito.Mock.class)
                    || null != field.getAnnotation(Mock.class)) {
                Object fieldInstance = null;
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    fieldInstance = field.get(testClass);
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Problems injecting dependencies in " + field.getName(), e);
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
