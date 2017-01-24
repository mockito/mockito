/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Set;
import org.mockito.Spy;
import org.mockito.internal.configuration.SpyFieldInitializer;

/**
 * Handler for field annotated with &#64;InjectMocks and &#64;Spy.
 *
 * <p>
 * The handler assumes that field initialization AND injection already happened.
 * So if the field is still null, then nothing will happen there.
 * </p>
 */
public class SpyOnInjectedFieldsHandler extends MockInjectionStrategy {

    @Override
    protected boolean processInjection(Field field, Object fieldOwner, Set<Object> ignored) {
        if (field.isAnnotationPresent(Spy.class)) {
            SpyFieldInitializer.initializeSpy(fieldOwner, field);
        }

        return false;
    }
}
