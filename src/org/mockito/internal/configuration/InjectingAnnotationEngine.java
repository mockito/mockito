/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.*;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldReader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * See {@link MockitoAnnotations}
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class InjectingAnnotationEngine implements AnnotationEngine {
    MockUtil mockUtil = new MockUtil();
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
     * &#064;InjectMocks for given testClassInstance.
     * <p>
     * See examples in javadoc for {@link MockitoAnnotations} class.
     * 
     * @param testClassInstance
     *            Test class, usually <code>this</code>
     */
    public void injectMocks(final Object testClassInstance) {
        Class<?> clazz = testClassInstance.getClass();
        Set<Field> mockDependentFields = new HashSet<Field>();
        Set<Object> mocks = new HashSet<Object>();
        
        while (clazz != Object.class) {
            mockDependentFields.addAll(scanForInjection(testClassInstance, clazz));
            mocks.addAll(scanAndPrepareMocks(testClassInstance, clazz));
            clazz = clazz.getSuperclass();
        }
        
        new DefaultInjectionEngine().injectMocksOnFields(mockDependentFields, mocks, testClassInstance);
    }

    /**
     * Scan fields annotated by &#064;InjectMocks
     *
     * @param testClassInstance Instance of the test
     * @param clazz Current class in the hierarchy of the test
     * @return Fields that depends on Mock
     *
     * @see #scanAndPrepareMocks(Object, Class)
     */
    private Set<Field> scanForInjection(final Object testClassInstance, final Class<?> clazz) {
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

    /**
     * Scan mocks for the given <code>testClassInstance</code> and <code>clazz</code> in the type hierarchy.
     *
     * <p>
     *     Actually the preparation of mocks consists only in defining a MockName if not already set.
     * </p>
     *
     * @param testClassInstance The test instance
     * @param clazz The class in the type hierarchy of this instance.
     * @return A prepared set of mock
     */
    private Set<Object> scanAndPrepareMocks(final Object testClassInstance, final Class<?> clazz) {
        Set<Object> mocks = new HashSet<Object>();
        for (Field field : clazz.getDeclaredFields()) {
            // mock or spies only
            FieldReader fieldReader = new FieldReader(testClassInstance, field);
            if (containsMockOrSpy(field, fieldReader)) {
                Object mockInstance = fieldReader.read();

                if (mockInstance != null) {
                    mockUtil.redefineMockNameIfSurrogate(mockInstance, field.getName());
                    mocks.add(mockInstance);
                }
            }
        }
        return mocks;
    }

    private boolean containsMockOrSpy(Field field, FieldReader fieldReader) {
        return null != field.getAnnotation(Spy.class)
                || null != field.getAnnotation(Mock.class)
                || null != field.getAnnotation(MockitoAnnotations.Mock.class)
                || mockUtil.isMock(fieldReader.read())
                || mockUtil.isSpy(fieldReader.read());
    }
}
