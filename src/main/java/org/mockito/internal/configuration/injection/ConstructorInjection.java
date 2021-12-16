/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.mockito.internal.exceptions.Reporter.fieldInitialisationThrewException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.ConstructorResolver;
import org.mockito.internal.util.reflection.ConstructorResolver.BiggestConstructorResolver;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;

/**
 * Injection strategy based on constructor.
 *
 * <p>
 * The strategy will search for the constructor with most parameters
 * and try to resolve mocks by type, or null if there is no mocks matching a parameter.
 * </p>
 */
public class ConstructorInjection extends MockInjectionStrategy {

    @Override
    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        try {
            ConstructorResolver constructorResolver =
                    createConstructorResolver(field.getType(), mockCandidates);
            FieldInitializationReport report =
                    new FieldInitializer(fieldOwner, field, constructorResolver).initialize();

            return report.fieldWasInitialized();
        } catch (MockitoException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                Throwable realCause = e.getCause().getCause();
                throw fieldInitialisationThrewException(field, realCause);
            }
            // other causes should be fine
            return false;
        }
    }

    protected ConstructorResolver createConstructorResolver(
            Class<?> fieldType, Set<Object> mockCandidates) {
        return new BiggestConstructorResolver(fieldType, mockCandidates);
    }
}
