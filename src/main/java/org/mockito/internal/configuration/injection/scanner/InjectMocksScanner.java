/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.scanner;

import static org.mockito.internal.exceptions.Reporter.unsupportedCombinationOfAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Scan field for injection.
 */
public class InjectMocksScanner {
    private final Class<?> clazz;

    /**
     * Create a new InjectMocksScanner for the given clazz on the given instance
     *
     * @param clazz    Current class in the hierarchy of the test
     */
    public InjectMocksScanner(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Scan the class hierarchy to find fields annotated by @{@link InjectMocks}.
     *
     * @return A set of fields annotated by @{@link InjectMocks}
     */
    public Set<Field> scanHierarchy() {
        final Set<Field> mockDependentFields = new HashSet<>();
        Class<?> currentClass = clazz;
        while (currentClass != Object.class) {
            scan(currentClass, mockDependentFields);
            currentClass = currentClass.getSuperclass();
        }
        return mockDependentFields;
    }

    /**
     * Scan fields annotated by &#064;InjectMocks
     */
    @SuppressWarnings("unchecked")
    private void scan(Class<?> clazz, Set<Field> mockDependentFields) {
        for (Field field : clazz.getDeclaredFields()) {
            if (null != field.getAnnotation(InjectMocks.class)) {
                assertNoAnnotations(field, Mock.class, Captor.class);
                mockDependentFields.add(field);
            }
        }
    }

    private static void assertNoAnnotations(
            Field field, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (field.isAnnotationPresent(annotation)) {
                throw unsupportedCombinationOfAnnotations(
                        annotation.getSimpleName(), InjectMocks.class.getSimpleName());
            }
        }
    }
}
