/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.*;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.internal.configuration.injection.scanner.InjectMocksScanner;
import org.mockito.internal.configuration.injection.scanner.MockScanner;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

/**
 * See {@link MockitoAnnotations}
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class InjectingAnnotationEngine implements AnnotationEngine {
    private final AnnotationEngine delegate = new DefaultAnnotationEngine();
    private final AnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

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
        Set<Object> mocks = newMockSafeHashSet();
        
        while (clazz != Object.class) {
            new InjectMocksScanner(clazz).addTo(mockDependentFields);
            new MockScanner(testClassInstance, clazz).addPreparedMocks(mocks);
            onInjection(testClassInstance, clazz, mockDependentFields, mocks);
            clazz = clazz.getSuperclass();
        }
        
        new DefaultInjectionEngine().injectMocksOnFields(mockDependentFields, mocks, testClassInstance);
    }

    protected void onInjection(Object testClassInstance, Class<?> clazz, Set<Field> mockDependentFields, Set<Object> mocks) {

    }

}
