/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.scanner.InjectMocksScanner;
import org.mockito.internal.configuration.injection.scanner.MockScanner;
import org.mockito.plugins.AnnotationEngine;

/**
 * See {@link MockitoAnnotations}
 */
public class InjectingAnnotationEngine
        implements AnnotationEngine, org.mockito.configuration.AnnotationEngine {
    private final AnnotationEngine delegate = new IndependentAnnotationEngine();
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
     * @see org.mockito.plugins.AnnotationEngine#process(Class, Object)
     */
    public AutoCloseable process(Class<?> clazz, Object testInstance) {
        List<AutoCloseable> closeables = new ArrayList<>();
        closeables.addAll(processIndependentAnnotations(testInstance.getClass(), testInstance));
        closeables.addAll(processInjectMocks(testInstance.getClass(), testInstance));
        return () -> {
            for (AutoCloseable closeable : closeables) {
                closeable.close();
            }
        };
    }

    private List<AutoCloseable> processInjectMocks(
            final Class<?> clazz, final Object testInstance) {
        List<AutoCloseable> closeables = new ArrayList<>();
        Class<?> classContext = clazz;
        while (classContext != Object.class) {
            closeables.add(injectMocks(testInstance));
            classContext = classContext.getSuperclass();
        }
        return closeables;
    }

    private List<AutoCloseable> processIndependentAnnotations(
            final Class<?> clazz, final Object testInstance) {
        List<AutoCloseable> closeables = new ArrayList<>();
        Class<?> classContext = clazz;
        while (classContext != Object.class) {
            // this will create @Mocks, @Captors, etc:
            closeables.add(delegate.process(classContext, testInstance));
            // this will create @Spies:
            closeables.add(spyAnnotationEngine.process(classContext, testInstance));

            classContext = classContext.getSuperclass();
        }
        return closeables;
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
    private AutoCloseable injectMocks(final Object testClassInstance) {
        Class<?> clazz = testClassInstance.getClass();
        Set<Field> mockDependentFields = new HashSet<Field>();
        Set<Object> mocks = newMockSafeHashSet();

        while (clazz != Object.class) {
            new InjectMocksScanner(clazz).addTo(mockDependentFields);
            new MockScanner(testClassInstance, clazz).addPreparedMocks(mocks);
            onInjection(testClassInstance, clazz, mockDependentFields, mocks);
            clazz = clazz.getSuperclass();
        }

        new DefaultInjectionEngine()
                .injectMocksOnFields(mockDependentFields, mocks, testClassInstance);

        return () -> {
            for (Object mock : mocks) {
                if (mock instanceof MockedStatic<?>) {
                    ((MockedStatic<?>) mock).closeOnDemand();
                }
            }
        };
    }

    protected void onInjection(
            Object testClassInstance,
            Class<?> clazz,
            Set<Field> mockDependentFields,
            Set<Object> mocks) {}
}
