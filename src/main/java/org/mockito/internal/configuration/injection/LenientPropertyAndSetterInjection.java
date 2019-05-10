/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import org.mockito.internal.util.reflection.ConstructorResolver;
import org.mockito.internal.util.reflection.ConstructorResolver.LenientNoArgsConstructorResolver;

/**
 * Inject mocks using setters then fields, if no setters available, see
 * {@link PropertyAndSetterInjection parent class} for more information on algorithm.
 * <p>
 * The strategy to instantiate field (if needed) is to try to find no-args constructor on field type
 * and skip the field otherwise.
 * </p>
 *
 * @see org.mockito.internal.configuration.injection.PropertyAndSetterInjection
 */
public class LenientPropertyAndSetterInjection extends PropertyAndSetterInjection {

    @Override
    protected ConstructorResolver createConstructorResolver(Class<?> fieldType) {
        return new LenientNoArgsConstructorResolver(fieldType);
    }

}
