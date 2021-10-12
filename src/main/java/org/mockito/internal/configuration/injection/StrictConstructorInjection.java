/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import java.util.Set;

import org.mockito.internal.util.reflection.ConstructorResolver;
import org.mockito.internal.util.reflection.ConstructorResolver.StrictBiggestConstructorResolver;

/**
 * Injection strategy based on constructor.
 * <p>
 * The strategy will search for the constructor with most parameters and try to resolve mocks by
 * type or skip the field if there is no mocks matching a parameter.
 * </p>
 */
public class StrictConstructorInjection extends ConstructorInjection {

    @Override
    protected ConstructorResolver createConstructorResolver(
            Class<?> fieldType, Set<Object> mockCandidates) {
        return new StrictBiggestConstructorResolver(fieldType, mockCandidates);
    }
}
