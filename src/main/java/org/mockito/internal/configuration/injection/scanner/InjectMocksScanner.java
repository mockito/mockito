/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.scanner;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.internal.exceptions.Reporter.unsupportedCombinationOfAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

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
     * Add the fields annotated by @{@link InjectMocks}
     *
     * @param mockDependentFields Set of fields annotated by  @{@link InjectMocks}
     */
    public void addTo(Set<Field> mockDependentFields) {
        mockDependentFields.addAll(scan());
    }

    /**
     * Scan fields annotated by &#064;InjectMocks
     *
     * @return Fields that depends on Mock
     */
    @SuppressWarnings("unchecked")
    private Set<Field> scan() {
        Set<Field> mockDependentFields = new HashSet<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(InjectMocks.class)) {
                assertNoAnnotations(field, Mock.class, Captor.class);
                mockDependentFields.add(field);
            }
        }

        return mockDependentFields;
    }

    private static void assertNoAnnotations(Field field, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (field.isAnnotationPresent(annotation)) {
                throw unsupportedCombinationOfAnnotations(annotation.getSimpleName(), InjectMocks.class.getSimpleName());
            }
        }
    }
}
