/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.fieldscanner;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.Reporter;

import java.lang.reflect.Field;

/**
 * Scan all the fields for the {@link InjectMocks} annotation and assert that no other conflicting
 * annotations exist.
 */
public class InjectMocksScanner extends FieldScanner<Field> {

    public InjectMocksScanner(Object testClassInstance) {
        super(testClassInstance);
    }

    @Override
    protected boolean hasCorrectAnnotation(Field field) {
        return field.getAnnotation(InjectMocks.class) != null;
    }

    @Override
    protected Field getObjectToAdd(Field field) {
        assertNoAnnotations(InjectMocks.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
        return field;
    }
}
